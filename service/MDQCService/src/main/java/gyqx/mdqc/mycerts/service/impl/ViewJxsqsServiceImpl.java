package gyqx.mdqc.mycerts.service.impl;

import common.db.query.QueryResult;
import common.utils.UtilsContext;
import gyqx.mdqc.mycerts.Constance;
import gyqx.mdqc.mycerts.dao.ViewJxsqsDao;
import gyqx.mdqc.mycerts.service.ViewJxsqsService;
import gyqx.mdqc.mycerts.vo.*;
import gyqx.mdqc.po.BasRegistrationInfo;
import gyqx.mdqc.po.ProvRegistrationInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.zip.Deflater;

import static java.util.stream.Collectors.*;

/**
 * @Author liangwu
 * @Date 18-8-22 上午10:42
 */
@Service
public class ViewJxsqsServiceImpl implements ViewJxsqsService {
    private static String[] columns = {"ERP编码", "产品名称", "产品规格", "注册证编号", "注册证效期", "注册证效期来自JDE，建议更新注册证", "厂家", "是否关联授权书"};
    private final static ConcurrentMap<String, String> corpKindMap = new ConcurrentHashMap<>();

    @Autowired
    private UtilsContext utilsContext;
    @Autowired
    private ViewJxsqsDao dao;

    @Override
    public QueryResult<GoodsInfoVo> listGoodsInfo(String input, String mfrsId, String hosId, Integer page, Integer size) {
        String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        String kind = getCorpKind(corpId);
        return dao.listGoodsInfo(input, mfrsId, corpId, kind, hosId, page, size);
    }

    @Override
    public ByteArrayOutputStream getExcel(String hosId, String hosName) throws IOException {
        String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        List<GoodsCertInfoVo> result = dao.listGoodsInfoByHosId(corpId, hosId);
        result.parallelStream().forEach(vo -> {
            BasRegistrationInfo bas = Optional.ofNullable(vo.getBasCertList()).orElse(Collections.emptyList()).stream().max(Comparator.comparing(BasRegistrationInfo::getExpdtEndDate)).orElse(null);
            ProvRegistrationInfo prov = Optional.ofNullable(vo.getProvCertList()).orElse(Collections.emptyList()).stream().max(Comparator.comparing(ProvRegistrationInfo::getExpdtEndDate)).orElse(null);
            if (bas != null && prov != null) {
                if (bas.getExpdtEndDate().after(prov.getExpdtEndDate())) {
                    vo.setCertificateCode(bas.getCertificateCode());
                    vo.setExpdtEndDate(bas.getExpdtEndDate());
                    vo.setFromJDE(true);
                } else {
                    vo.setCertificateCode(prov.getCertificateCode());
                    vo.setExpdtEndDate(prov.getExpdtEndDate());
                    vo.setFromJDE(false);
                }
            } else if (bas != null && prov == null) {
                vo.setCertificateCode(bas.getCertificateCode());
                vo.setExpdtEndDate(bas.getExpdtEndDate());
                vo.setFromJDE(true);
            } else if (bas == null && prov != null) {
                vo.setCertificateCode(prov.getCertificateCode());
                vo.setExpdtEndDate(prov.getExpdtEndDate());
                vo.setFromJDE(false);
            }
        });
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(hosName);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (GoodsCertInfoVo item : result) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getErpCode());
            row.createCell(1).setCellValue(item.getGoodsName());
            row.createCell(2).setCellValue(item.getGoodsGg());
            row.createCell(3).setCellValue(item.getCertificateCode());
            row.createCell(4).setCellValue(item.getExpdtEndDate() != null ? DateFormatUtils.format(item.getExpdtEndDate(), "YYYY-MM-dd") : "");
            Cell cell = row.createCell(5);
            if (item.isFromJDE()) {
                cell.setCellValue("是");
                cell.setCellStyle(cellStyle);
            } else {
                cell.setCellValue("否");
            }
            row.createCell(6).setCellValue(item.getMfrsName());
            row.createCell(7).setCellValue(item.isTrackable() ? "是" : "否");
        }
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        workbook.write(byteOut);
        workbook.close();

        return byteOut;
    }

    @Override
    public ByteArrayOutputStream getCertsZipOutStream(String goodsId, String hosId) throws IOException {
        byte[] buffer = new byte[1024];

        final String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        String kind = getCorpKind(corpId);
        if (kind.equals(Constance.HOS)) {
            throw new IllegalArgumentException("医院用户暂时无法下载全部证件。");
        }

        // 经销授权书链条
        List<JxsqsData> jxsqsDataList = dao.getJxsqsDataList(corpId, kind, goodsId, null, hosId);
        // 授权单位列表，用于查找企业证照
        List<String> chiefIds = jxsqsDataList.stream().map(JxsqsData::getChiefId).collect(toList());
        // 授权书附件信息
        List<CertInfoVo> certInfoVoList = jxsqsDataList.stream()
                .map(item -> new CertInfoVo("授权书", item.getChiefName(), item.getProvName(), item.getFilePath()))
                .collect(toList());
        // 产品证照、企业证照信息
        certInfoVoList.addAll(dao.getAllCertInfo(goodsId, corpId, chiefIds));

        String basePath = this.getbasePath();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setLevel(Deflater.BEST_SPEED);
        zos.setEncoding("GBK");
        int i = 1;
        for (CertInfoVo item : certInfoVoList) {
            try {
                FileInputStream fis = new FileInputStream(FilenameUtils.concat(basePath, item.getFilePath()));
                String fileName = Integer.toString(i++) + item.toString() + "." + FilenameUtils.getExtension(item.getFilePath());
                ZipEntry ze = new ZipEntry(fileName);
                zos.putNextEntry(ze);
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException(item.toString().trim() + " 未找到相关附件。");
            }
        }
        zos.close();
        return bos;
    }

    @Override
    public List<TreeNode<JxsqsData>> getTrackingChain(String goodsId, String hosId) {
        final String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        String kind = getCorpKind(corpId);
        List<String> sqsIds = new ArrayList<>();
        if (kind.equals(Constance.HOS)) {
            sqsIds.addAll(dao.getSqsIdsByHosId(corpId));
        }
        List<JxsqsData> jxsqsDataList = dao.getJxsqsDataList(corpId, kind, goodsId, sqsIds, hosId);
        List<JxsqsData> rootNodeDatas;
        if (kind.equals(Constance.HOS)) {
            rootNodeDatas = jxsqsDataList.stream().filter(item -> sqsIds.contains(item.getSqsId())).collect(toList());
        } else {
            rootNodeDatas = jxsqsDataList.stream().filter(item -> item.getProvId().equals(corpId)).collect(Collectors.toList());
        }
//        jxsqsDataList.removeAll(rootNodeDatas);
        List<TreeNode<JxsqsData>> roots = new ArrayList<>();
        for (JxsqsData jxsqsData : rootNodeDatas) {
            TreeNode<JxsqsData> root = new TreeNode<>(jxsqsData);
            roots.add(root);
        }
        makeTree(roots, jxsqsDataList);
        return roots;
    }

    @Override
    public Map<String, List<String>> getSqsHos(Set<String> sqsIds) {
        String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        String kind = getCorpKind(corpId);
        return dao.getSqsHos(sqsIds, corpId, kind).stream().collect(groupingBy(IdNameVo::getId, mapping(IdNameVo::getName, toList())));
    }

    public String getCorpKind(String corpId) {
        String kind;
        if (corpKindMap.containsKey(corpId)) {
            kind = corpKindMap.get(corpId);
        } else {
            kind = dao.getCorpKind(corpId);
            corpKindMap.put(corpId, kind);
        }
        return kind;
    }

    private List<TreeNode<JxsqsData>> makeTree(List<TreeNode<JxsqsData>> nodes, List<JxsqsData> list) {
        for (TreeNode<JxsqsData> node : nodes) {
            JxsqsData curData = node.getData();
            List<JxsqsData> children;
            if (curData.getChiefId() != null && curData.getSqsLastlevelId() != null) {
                children = list.stream()
                        .filter(child ->
                                curData.getChiefId().equals(child.getProvId())
                                        && curData.getSqsLastlevelId().equals(child.getSqsId()))
                        .collect(Collectors.toList());
            } else {
                children = Collections.emptyList();
            }

            if (!children.isEmpty()) {
                for (JxsqsData child : children) {
                    node.addChild(child);
                }
//                list.removeAll(children);
                makeTree(node.getChildren(), list);
            }
        }
        return nodes;
    }

    private String getbasePath() {
        String basePath = utilsContext.getSysConfigUtil().getValue("upload.basepath");
        if (basePath.contains(";")) {
            String[] paths = basePath.split(";");
            return System.getProperty("os.name").toLowerCase().contains("window") ? paths[0] : paths[1];
        } else {
            return basePath;
        }
    }
}
