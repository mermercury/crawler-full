package cn.itcast.crawler.test;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import java.io.*;

public class generateWord {
    public static void main(String[] args) throws Exception {
        String title = "Title.txt"; // 标题文本文件路径
        String image = "image.jpg"; // 图片文件路径
        String content = "Content.txt"; // 内容文本文件路径
        String outputPath = "output.docx"; // 生成的Word文件路径
        for (int i = 0; i < 50; i++) {
            generateAWord("D:\\javaweb\\download\\titles\\title" + i + ".txt",
                    "D:\\javaweb\\download\\images\\image" + i + ".jpg",
                    "D:\\javaweb\\download\\content\\output" + i + ".txt",
                    "D:\\javaweb\\download\\docs\\output" + i + ".docx");
        }
    }

    private static void generateAWord(String title, String image, String content, String outputPath) throws Exception {
        try {
            // 创建一个新的Word文档对象
            CustomXWPFDocument document = new CustomXWPFDocument();

            // 创建标题段落
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(readTextFile(title)); // 从文本文件读取标题内容
            titleRun.setBold(true); // 设置加粗
            titleRun.setFontSize(16); // 设置字号

            // 设置段落对齐方式
            titleParagraph.setAlignment(ParagraphAlignment.CENTER); // 居中对齐

            // 添加换行
//            XWPFParagraph newlineParagraph = document.createParagraph();
//            newlineParagraph.createRun().addBreak();

            // 创建图片
            try {
                File imageFile = new File(image);
                FileInputStream imageStream = new FileInputStream(imageFile);
                XWPFParagraph imageParagraph = document.createParagraph();
                XWPFRun imageRun = imageParagraph.createRun();
                int width = Units.toEMU(400);  // 设置图片宽度
                int height = Units.toEMU(300); // 设置图片高度
                int pictureType = XWPFDocument.PICTURE_TYPE_JPEG; // 设置图片类型
                imageRun.addPicture(imageStream, pictureType, imageFile.getName(), width, height);
                imageRun.addBreak();
                imageStream.close();
            } catch (Exception e) {

            }
            // 创建内容段落
            XWPFParagraph contentParagraph = document.createParagraph();
            XWPFRun contentRun = contentParagraph.createRun();
            contentRun.setText(readTextFile(content)); // 从文本文件读取内容

            // 保存Word文档
            FileOutputStream outputStream = new FileOutputStream(outputPath);
            document.write(outputStream);
            outputStream.close();
            document.close();


            System.out.println("Word文件生成完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static void addPicture(CustomXWPFDocument document, XWPFRun run, InputStream imageStream, int pictureType, String imgName) throws Exception {
        int width = Units.toEMU(400);
        int height = Units.toEMU(300);

        // 添加图片到文档
        XWPFPicture picture = run.addPicture(imageStream, pictureType, imgName, width, height);

        // 调整图片位置和大小
        if (picture != null) {
            CTPicture ctPicture = picture.getCTPicture();
            ctPicture.getSpPr().addNewXfrm().addNewOff().setX(0);
            ctPicture.getSpPr().addNewXfrm().addNewOff().setY(0);
            ctPicture.getSpPr().addNewXfrm().addNewExt().setCx(width);
            ctPicture.getSpPr().addNewXfrm().addNewExt().setCy(height);
        }
    }

    public static class CustomXWPFDocument extends XWPFDocument {
        public CustomXWPFDocument(InputStream in) throws IOException {
            super(in);
        }

        public CustomXWPFDocument() {
            super();
        }

        public CustomXWPFDocument(OPCPackage pkg) throws IOException {
            super(pkg);
        }

        /**
         * @param id
         * @param width 宽
         * @param height 高
         * @param paragraph  段落
         */
//        public void createPicture(int id, int width, int height, XWPFParagraph paragraph) {
//            final int EMU = 9525;
//            width *= EMU;
//            height *= EMU;
//            String blipId = getAllPictures().get(id).getPackageRelationship().getId();
//            CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
//            String picXml = ""
//                    +"<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
//                    +"   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
//                    +"      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
//                    +"         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
//                    + id
//                    +"\" name=\"Generated\"/>"
//                    +"            <pic:cNvPicPr/>"
//                    +"         </pic:nvPicPr>"
//                    +"         <pic:blipFill>"
//                    +"            <a:blip r:embed=\""
//                    + blipId
//                    +"\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
//                    +"            <a:stretch>"
//                    +"               <a:fillRect/>"
//                    +"            </a:stretch>"
//                    +"         </pic:blipFill>"
//                    +"         <pic:spPr>"
//                    +"            <a:xfrm>"
//                    +"               <a:off x=\"0\" y=\"0\"/>"
//                    +"               <a:ext cx=\""
//                    + width
//                    +"\" cy=\""
//                    + height
//                    +"\"/>"
//                    +"            </a:xfrm>"
//                    +"            <a:prstGeom prst=\"rect\">"
//                    +"               <a:avLst/>"
//                    +"            </a:prstGeom>"
//                    +"         </pic:spPr>"
//                    +"      </pic:pic>"
//                    +"   </a:graphicData>" + "</a:graphic>";
//
//            inline.addNewGraphic().addNewGraphicData();
//            XmlToken xmlToken = null;
//            try{
//                xmlToken = XmlToken.Factory.parse(picXml);
//            }catch(XmlException xe) {
//                xe.printStackTrace();
//            }
//            inline.set(xmlToken);
//
//            inline.setDistT(0);
//            inline.setDistB(0);
//            inline.setDistL(0);
//            inline.setDistR(0);
//
//            CTPositiveSize2D extent = inline.addNewExtent();
//            extent.setCx(width);
//            extent.setCy(height);
//
//            CTNonVisualDrawingProps docPr = inline.addNewDocPr();
//            docPr.setId(id);
//            docPr.setName("Picture " + id);
//            docPr.setDescr("XWPFUtil Generated.");
//
//        }


    }
}
