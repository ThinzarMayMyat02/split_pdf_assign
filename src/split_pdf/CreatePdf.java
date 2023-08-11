package split_pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class CreatePdf {
	final static String source = "C:/Users/maymyatthinzar/sample_pdf/source_folder";
	final static String destination = "C:/Users/maymyatthinzar/sample_pdf/splitted_folder/";
	final static String done = "C:/Users/maymyatthinzar/sample_pdf/done_folder/";
	public static int numOfPage = 1;
	private static LocalDate date = LocalDate.now();
	private static DateTimeFormatter formated = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static void main(String[] args) throws IOException {
		System.out.println("start main method..");
		if (numOfPage > 0) {
			readFileFromResource(source, destination, done);
		}
		System.out.println("end main method..");
	}

	public static void readFileFromResource(String source, String destination, String done) throws IOException {
		File f = new File(source);
		File listFile[] = f.listFiles();
		for (File file : listFile) {
			File filePath = file;
			String newFolderName = FilenameUtils.getBaseName(filePath.getName());
			if (file.getName().endsWith(".pdf")) {
				/* check exist or not pdf directory	*/
				if (filePath != null && filePath.exists()) {
					/*  making new folder */
					File destinationpath = new File(destination, newFolderName);
					destinationpath.mkdirs();
					/*  loading pdf	*/
					PDDocument document = PDDocument.load(filePath);
					/*  splitting pdf */
					List<String>nameList=splitPdfPerPage(document, numOfPage, destination, destinationpath.getName());

					/*  add index file */
					addIndexFile(newFolderName, destination + newFolderName, nameList);
					document.close();
				}

				/* move to done folder from resource folder */
				System.out.println("current: " + file.getAbsolutePath());
				File doneFile = new File(done + "/" + file.getAbsoluteFile().getName());
				System.out.println("reach: " + doneFile.getAbsolutePath());
				//FileUtils.copyFile(file, doneFile);
				Files.copy(file.toPath(),doneFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
				file.deleteOnExit();
			}

		}
		
	}

	public static List<String> splitPdfPerPage(PDDocument doc, int numOfSplitPage,
											String destination,String foldername)throws IOException {
		List<String> nameOfFiles = new ArrayList<>();
		int docPages = countPages(doc);
		if (doc.getNumberOfPages() > 0) {
			try {
				/*	get splitter  */
				Splitter splitter = new Splitter();
				splitter.setSplitAtPage(numOfSplitPage);
				List<PDDocument> splitedDoc = splitter.split(doc);
				int size = 1;
				Iterator<PDDocument> iterator = splitedDoc.listIterator();
				while (iterator.hasNext()) {
					PDDocument pages = iterator.next();

					String formatDate = date.format(formated);
					String desc = destination + foldername;
					String name = foldername + "_" + formatDate + "_" +String.format("%0" + docPages + "d", size++) + ".pdf";

					pages.save(desc + "/" + name);
					nameOfFiles.add(name);
					pages.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			doc.close();
		}
		return nameOfFiles;
	}

	public static int countPages(PDDocument doct) throws IOException {
		int i = doct.getNumberOfPages();
		int count = 0;
		while (i != 0) {
			i = i / 10;
			++count;
		}
		doct.close();
		return count;
	}

	public static void addIndexFile(String foldername, String path,List<String>nameOfFiles) {
		String tem = path + "/";
		try {
			/* add text file called index */
			File fil = new File(tem, "index_" + foldername + "_" + date.format(formated) + ".txt");
			FileWriter fwrite = new FileWriter(fil);
			for (String fn : nameOfFiles) {
				fwrite.write(fn + " | Split pdf successful.\n");
			}
			fwrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
