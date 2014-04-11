package com.dm.system;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

@Model
public class JcrBean {

	@Resource(name = "jcr/Repository")
	private Repository repository;

	public void testJcr() throws Exception {
		Credentials credentials = new SimpleCredentials("admin",
				"admin".toCharArray());
		final Session jcrSession;
		try {
			jcrSession = repository.login(credentials, null);
			Workspace ws = jcrSession.getWorkspace();
			ws.getNamespaceRegistry().registerNamespace("wiki",
					"http://www.barik.net/wiki/1.0");
		} catch (RepositoryException e) {
			e.printStackTrace();
			return;
		}

		try {
			String seedWord = "";
			int numDocs = 0;
			List<String> filetypes = Arrays.asList(new String[] { "pdf", "rtf",
					"doc", "ppt", "xls" });

			Node root = jcrSession.getRootNode();
			int n = 0;
			for (int typeIdx = 0; typeIdx < filetypes.size(); typeIdx++) {
				String type = (String) filetypes.get(typeIdx);
				int offset = 0;
				while (n < numDocs * (typeIdx + 1) / filetypes.size()) {
					final URL[] urls = new Search(type, seedWord, offset)
							.getURLs();
					if (urls.length == 0) {
						break;
					}
					for (int i = 0; i < urls.length; i++) {
						final URL currentURL = urls[i];
						String path = urls[i].getPath();
						if (path.startsWith("/")) {
							path = path.substring(1);
						}
						final String host = urls[i].getHost();
						List<String> folderNames = new ArrayList<>();
						folderNames.addAll(Arrays.asList(host.split("\\.")));
						Collections.reverse(folderNames);
						folderNames.addAll(Arrays.asList(path.split("/", 0)));
						final String fileName = URLDecoder
								.decode((String) folderNames.remove(folderNames
										.size() - 1), "UTF-8").replaceAll(":",
										"_");
						Node node = root;
						for (Iterator<String> fn = folderNames.iterator(); fn
								.hasNext();) {
							String name = URLDecoder.decode(fn.next(), "UTF-8");
							name = name.replaceAll(":", "_");
							if (name.length() == 0) {
								continue;
							}
							if (!node.hasNode(name)) {
								node.addNode(name, "nt:folder");
							}
							node = node.getNode(name);
						}
						if (!node.hasNode(fileName)) {
							final PrintStream fOut = System.out;
							Node file = node.addNode(fileName, "nt:file");
							final Node resource = file.addNode("jcr:content",
									"nt:resource");
							final Exception[] ex = new Exception[1];
							Thread t = new Thread(new Runnable() {
								public void run() {
									try {
										String info = fileName + " (" + host
												+ ")";
										URLConnection con = currentURL
												.openConnection();
										InputStream in = con.getInputStream();
										try {
											synchronized (fOut) {
												fOut.println("<script>dp.inform(0, '"
														+ info + "')</script>");
												fOut.flush();
											}
											int length = con.getContentLength();
											if (length != -1) {
												in = new ProgressInputStream(
														in, length, info, "dp",
														fOut);
											}
											Binary binary = jcrSession
													.getValueFactory()
													.createBinary(in);
											resource.setProperty("jcr:data",
													binary);
											String mimeType = URLConnection
													.guessContentTypeFromName(fileName);
											if (mimeType == null) {
												if (fileName.endsWith(".doc")) {
													mimeType = "application/msword";
												} else if (fileName
														.endsWith(".xls")) {
													mimeType = "application/vnd.ms-excel";
												} else if (fileName
														.endsWith(".ppt")) {
													mimeType = "application/mspowerpoint";
												} else {
													mimeType = "application/octet-stream";
												}
											}
											resource.setProperty(
													"jcr:mimeType", mimeType);
											Calendar lastModified = Calendar
													.getInstance();
											lastModified.setTimeInMillis(con
													.getLastModified());
											resource.setProperty(
													"jcr:lastModified",
													lastModified);
										} finally {
											in.close();
										}
									} catch (Exception e) {
										ex[0] = e;
									}
								}
							});
							t.start();
							for (int s = 0; t.isAlive(); s++) {
								Thread.sleep(100);
								if (s % 10 == 0) {
									synchronized (fOut) {
										fOut.println("<script>pb.inform(" + n
												+ ", '')</script>");
										fOut.flush();
									}
								}
							}
							if (ex[0] == null) {
								jcrSession.save();
								n++;
								synchronized (fOut) {
									fOut.println("<script>pb.inform(" + n
											+ ", '')</script>");
									fOut.flush();
								}
								if (n >= numDocs * (typeIdx + 1)
										/ filetypes.size()) {
									break;
								}
							} else {
								jcrSession.refresh(false);
							}
						}
					}
					offset += 10;
				}
			}
		} finally {
			if (jcrSession != null) {
				jcrSession.logout();
			}
		}
	}

	public static class Search {

		private final String filetype;

		private final String term;

		private final int start;

		public Search(String filetype, String term, int start) {
			this.filetype = filetype;
			this.term = term;
			this.start = start;
		}

		public URL[] getURLs() throws Exception {
			List<URL> urls = new ArrayList<>();
			String query = term + " filetype:" + filetype;
			URL google = new URL("http://www.google.com/search?q="
					+ URLEncoder.encode(query, "UTF-8") + "&start=" + start);
			URLConnection con = google.openConnection();
			con.setRequestProperty("User-Agent", "");
			InputStream in = con.getInputStream();
			try {
				HTMLEditorKit kit = new HTMLEditorKit();
				HTMLDocument doc = new HTMLDocument();
				doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
				kit.read(new InputStreamReader(in, "UTF-8"), doc, 0);
				HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
				while (it.isValid()) {
					AttributeSet attr = it.getAttributes();
					if (attr != null) {
						String href = (String) attr
								.getAttribute(HTML.Attribute.HREF);
						if (href != null && href.endsWith("." + filetype)) {
							URL url = new URL(new URL("http", "www.google.com",
									"dummy"), href);
							if (url.getHost().indexOf("google") == -1) {
								urls.add(url);
							}
						}
					}
					it.next();
				}
			} finally {
				in.close();
			}
			return (URL[]) urls.toArray(new URL[urls.size()]);
		}
	}

	public static class ProgressInputStream extends FilterInputStream {

		private final int length;

		private final String fileName;

		private final String varName;

		private final PrintStream out;

		private long read;

		private long nextReport = (16 * 1024);

		public ProgressInputStream(InputStream in, int length, String fileName,
				String varName, PrintStream out) {
			super(in);
			this.length = length;
			this.fileName = fileName;
			this.varName = varName;
			this.out = out;
		}

		public int read() throws IOException {
			int r = super.read();
			reportProgress(r);
			return r;
		}

		public int read(byte b[]) throws IOException {
			int r = super.read(b);
			reportProgress(r);
			return r;
		}

		public int read(byte b[], int off, int len) throws IOException {
			int r = super.read(b, off, len);
			reportProgress(r);
			return r;
		}

		private void reportProgress(int r) throws IOException {
			if (r != -1) {
				read += r;
				if (read > nextReport || read == length) {
					// report every 16k
					synchronized (out) {
						double s = 1000d * (double) read / (double) length;
						out.println("<script>" + varName + ".inform("
								+ Math.min((int) Math.ceil(s), 1000) + ", '"
								+ fileName + "')</script>");
						out.flush();
					}
					nextReport += (16 * 1024);
				}
			}
		}
	}
}