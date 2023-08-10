package split_pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class CreatePdf {
	public static int numOfPage =3;

	public static void main(String[] args) throws IOException {
		System.out.println("start main method..");
		final String source = "C:/Users/maymyatthinzar/sample_pdf/source_folder";
		final String destination = "C:/Users/maymyatthinzar/sample_pdf/splitted_folder/";
		if (numOfPage > 0) {
			readFileFromResource(source, destination);
		}
		System.out.println("end main method..");
	}

	public static void readFileFromResource(String source, String destination) throws IOException {
		File f = new File(source);
		File listFile[] = f.listFiles();
		for (File file : listFile) {
			
			if (file.getName().endsWith(".pdf")) {
				System.out.println("checking pdf or not condition......");
				File filePath = file;
				// check exist or not pdf directory
				if (filePath != null && filePath.exists()) {
					System.out.println("inside condition........");
					// making new folder
					String newFolderName = FilenameUtils.getBaseName(filePath.getName());
					File destinationpath = new File(destination, newFolderName);
					destinationpath.mkdirs();
					String foldername = destinationpath.getName();
					// add index file
					System.out.println("new folder path :"+destinationpath);
					System.out.println("folder name : "+foldername);
					addIndexFile(newFolderName, destinationpath.getAbsolutePath());
					// loading pdf
					PDDocument document = PDDocument.load(filePath);
					// splitting pdf
					splitPdfPerPage(document, numOfPage, destination, foldername);
				}
			}
		}
	}

	public static void splitPdfPerPage(PDDocument doc, int numOfSplitPage, String destinationpath, String foldername)
			throws IOException {
		int docPages = countPages(doc);
		if (doc.getNumberOfPages() > 0) {
			try {
				Splitter splitter = new Splitter();
				splitter.setSplitAtPage(numOfSplitPage);
				List<PDDocument> splitedDoc = splitter.split(doc);
				int size = 1;
				Iterator<PDDocument> iterator = splitedDoc.listIterator();
				while (iterator.hasNext()) {
					PDDocument pages = iterator.next();
					LocalDate date = LocalDate.now();
					DateTimeFormatter formated = DateTimeFormatter.ofPattern("yyyyMMdd");
					String formatDate = date.format(formated);
					String desc = destinationpath + foldername;
					pages.save(desc + "/" + foldername + "_" + formatDate + "_"
							+ String.format("%0" + docPages + "d", size++) + ".pdf");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			doc.close();
		}
	}

	public static int countPages(PDDocument doct) {
		int i = doct.getNumberOfPages();
		int count = 0;
		while (i != 0) {
			i = i / 10;
			++count;
		}
		return count;
	}

	public static void addIndexFile(String foldername, String path) {
		System.out.println("param: "+foldername +"\n param path: "+path);
		String tem = path + "/" ;
		File f = new File(path);
		System.out.println("file path: " + f.getAbsolutePath());
		try {
			File fil = new File(tem, "index.txt");
			FileWriter fwrite = new FileWriter(fil);
			fwrite.write("data writing..........");
			System.out.println("success ");
			// while(path != null){
			// File Lfiles=f.listFiles();
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
