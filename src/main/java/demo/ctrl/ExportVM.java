package demo.ctrl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.chart.Exporting;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zul.Filedownload;

import demo.model.MarketShare;
import demo.model.MarketShareDAO;

public class ExportVM {

	private DefaultCategoryModel model = new DefaultCategoryModel();
	private String title;

	@Init
	public void init() {
		title = "Market Share";
		List<MarketShare> list = MarketShareDAO.filterYearLocation(new int[] { 2014, 2015 },
				new String[] { "New York", "California", "L.A." });
		for (MarketShare share : list) {
			model.setValue(share.getYear(), share.getLocation(), share.getShare());
		}
	}

	@Command
	public void initChart(@BindingParam("exportOption") Exporting options) {
		options.setEnabled(false);
	}

	@Command
	public void export(@BindingParam("fmt") String fmt,
			@BindingParam("title") String title,
			@BindingParam("svg") String svgContent) throws IOException {
		String fileName = title.toLowerCase().replaceAll(" ", "_");
		String tDir = System.getProperty("java.io.tmpdir"); //create file in temp folder
		Path svgFile = Paths.get(tDir, fileName + ".svg");
		if (!Files.exists(svgFile)) {
			svgFile = Files.createFile(svgFile);
			Files.write(svgFile, svgContent.getBytes());
		}

		if (fmt.equals("svg")) {
			Filedownload.save(svgFile.toFile(), null);
		} else {
			Path output = Paths.get(tDir, fileName + "." + fmt);
			Files.deleteIfExists(output);

			IMOperation op = new IMOperation();
			op.addImage(svgFile.toFile().getPath());
			op.addImage(output.toFile().getPath());

			ConvertCmd converter = new ConvertCmd();
			try {
				converter.run(op);
				Filedownload.save(output.toFile(), null);
			} catch (InterruptedException | IM4JavaException e) {
				e.printStackTrace();
			}
		}
	}

	public DefaultCategoryModel getModel() {
		return model;
	}

	public String getTitle() {
		return title;
	}
}
