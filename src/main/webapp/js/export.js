zk.afterMount(function() {
	Highcharts.getMergedSVG = function(charts) {
		var svgArr = [],
			top = 0,
			width = 0;

		jq.each(charts, function(i, chart) {
			var svg = chart.getSVG();
			svg = svg.replace('<svg', '<g transform="translate(0,' + top + ')" ');
			svg = svg.replace('</svg>', '</g>');

			top += chart.chartHeight;
			width = Math.max(width, chart.chartWidth);

			svgArr.push(svg);
		});

		return '<svg height="' + top + '" width="' + width
				+ '" version="1.1" xmlns="http://www.w3.org/2000/svg">'
				+ svgArr.join('') + '</svg>';
	};
});
function fireExportEvent (wgt, id) {
	if (!id)
		return; // nothing to export
	
	var svg;
	if (jq.isArray(id)) {
		var charts = [];
		for (var i = 0, len = id.length; i < len; i++) {
			charts[i] = zk.Widget.$('$' + id[i]).engine;
		}
		svg = Highcharts.getMergedSVG(charts);
	} else {
		svg = zk.Widget.$('$' + id).engine.getSVG();
	}
	zAu.send(new zk.Event(wgt, 'onExport', {svg: svg}, {toServer: true}));
}
