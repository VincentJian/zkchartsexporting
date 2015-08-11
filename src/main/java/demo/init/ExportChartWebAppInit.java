package demo.init;

import org.im4java.process.ProcessStarter;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

public class ExportChartWebAppInit implements WebAppInit {

	@Override
	public void init(WebApp wapp) throws Exception {
		// set the converter.exe file path for im4java
		// converter.exe file is downloaded from ImageMagick (http://www.imagemagick.org/script/binary-releases.php)
		String magickPath = wapp.getRealPath("/WEB-INF/image-magick");
		ProcessStarter.setGlobalSearchPath(magickPath);
	}

}
