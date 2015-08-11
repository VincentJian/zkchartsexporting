package demo.ctrl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.zkoss.chart.Charts;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;

import demo.model.MarketShare;
import demo.model.MarketShareDAO;

public class ExportComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6788997068068377644L;

	@Wire
	private Charts chart1, chart2;
	@Wire
	private Listbox format;

	private DefaultCategoryModel model = new DefaultCategoryModel();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List<MarketShare> list = MarketShareDAO.filterYearLocation(new int[] { 2014, 2015 },
				new String[] { "New York", "California", "L.A." });
		for (MarketShare share : list) {
			model.setValue(share.getYear(), share.getLocation(), share.getShare());
		}
		chart1.setModel(model);
		chart1.setTitle("Market Share");
		chart1.getExporting().setEnabled(false);
		chart2.setModel(model);
		chart2.setTitle("Market Share");
		chart2.getExporting().setEnabled(false);
	}

	@SuppressWarnings("unchecked")
	@Listen("onExport = #export1, #export2")
	public void export(Event evt) throws IOException {
		String fmt = format.getSelectedItem().getLabel();
		String fileName = chart1.getTitle().getText().toLowerCase().replaceAll(" ", "_");
		String tDir = System.getProperty("java.io.tmpdir");
		Path svgFile = Paths.get(tDir, fileName + ".svg");
		Files.deleteIfExists(svgFile);
		Map<String, String> data = (Map<String, String>) evt.getData();
		String svgContent = data.get("svg");
		svgFile = Files.createFile(svgFile);
		Files.write(svgFile, svgContent.getBytes());

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

}
