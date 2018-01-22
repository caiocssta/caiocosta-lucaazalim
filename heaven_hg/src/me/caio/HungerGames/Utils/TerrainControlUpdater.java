package me.caio.HungerGames.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.plugin.Plugin;

public class TerrainControlUpdater {
	private boolean running;
	private Plugin plugin;
	private String versaoAtual;
	private String versaoUpdate;
	private String downloadURL = "http://pma.wombocraft.com.br/test/update/";
	private File updateFolder;
	private boolean needUpdate = true;

	public TerrainControlUpdater(Plugin plugin) {
		this.plugin = plugin;
		this.updateFolder = new File(plugin.getDataFolder().getAbsolutePath().replace("plugins\\WomboCraft_HungerGames", "plugins").replace("plugins/WomboCraft_HungerGames", "plugins"));
		loadActualVersion();
	}

	private void loadActualVersion() {
		try {
			File folder = this.plugin.getDataFolder();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(folder.getAbsoluteFile(), "TerrainControl.txt");
			if (!file.exists()) {
				file.createNewFile();
				OutputStreamWriter ostream = new OutputStreamWriter(new FileOutputStream(file));
				ostream.write("1.0");
				this.versaoAtual = "1.0";
				ostream.close();
				return;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				this.versaoAtual = inputLine;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setVersion(String version) {
		try {
			File folder = this.plugin.getDataFolder();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(folder.getAbsoluteFile(), "TerrainControl.txt");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			OutputStreamWriter ostream = new OutputStreamWriter(new FileOutputStream(file));
			ostream.write(version);
			this.versaoAtual = version;
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		if (!this.needUpdate) {
			return;
		}
		if (this.running) {
			return;
		}
		this.running = true;
		try {
			URL url = new URL(this.downloadURL + "TerrainControl/version.txt");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				this.versaoUpdate = inputLine;
			}
			if (this.versaoAtual.equals(this.versaoUpdate)) {
				System.out.println(" ");
				System.out.println("TerrainControl: Atualizado.");
				System.out.println(" ");
				this.running = false;
				return;
			}
			downloadUpdate();
		} catch (Exception e) {
			System.out.println(" ");
			System.out.println("TerrainControl: Falha ao atualizar.");
			System.out.println(this.downloadURL);
			System.out.println(" ");
			e.printStackTrace();
			this.running = false;
		}
	}

	private void downloadUpdate() {
		try {
			File to = new File(this.updateFolder, "TerrainControl.zip");
			File tmp = new File(to.getPath() + ".au");
			if (!tmp.exists()) {
				this.plugin.getDataFolder().mkdirs();
				tmp.createNewFile();
			}
			URL url = new URL(this.downloadURL + "TerrainControl/TerrainControl.zip");
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(tmp);
			byte[] buffer = new byte[4096];
			int fetched;
			while ((fetched = is.read(buffer)) != -1) {
				os.write(buffer, 0, fetched);
			}
			is.close();
			os.flush();
			os.close();
			if (to.exists()) {
				to.delete();
			}
			tmp.renameTo(to);
			deleteDir(new File(this.updateFolder, "TerrainControl"));
			unzip(to.getAbsolutePath(), this.updateFolder.getAbsolutePath());
			deleteDir(to);

			System.out.println(" ");
			System.out.println("TerrainControl: Atualização baixada com sucesso.");
			System.out.println(" ");
			this.needUpdate = false;
			this.running = false;
			setVersion(this.versaoUpdate);
		} catch (Exception e) {
			System.out.println(" ");
			System.out.println("TerrainControl: Falha ao atualizar.");
			System.out.println(" ");
			e.printStackTrace();
			this.running = false;
		}
	}

	private void unzip(String zipFile, String destDir) {
		try {
			File file = new File(zipFile);
			ZipFile zip = new ZipFile(file);
			File dir = new File(destDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			Enumeration<? extends ZipEntry> zipEntries = zip.entries();
			while (zipEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(dir, currentEntry);
				File dirParent = destFile.getParentFile();
				dirParent.mkdirs();
				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));

					byte[] data = new byte[4096];
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream bos = new BufferedOutputStream(fos, 4096);
					int currentByte;
					while ((currentByte = is.read(data, 0, 4096)) != -1) {
						bos.write(data, 0, currentByte);
					}
					bos.flush();
					bos.close();
					is.close();
				}
			}
			zip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		file.delete();
	}
}
