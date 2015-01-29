var week = [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ];
var canLog = true;

function showLog(str, info) {
	if (canLog) {
		var log = $("#log");
		if (!info)
			log.append("<li class='info'>" + getTime() + ":" + str + "</li>");
		else
			log.append("<li class='" + info + "'>" + getTime() + ":" + str
					+ "</li>");
		if (log.children("li").length > 20) {
			log.get(0).removeChild(log.children("li")[0]);
		}
	}
}
function getTime() {
	var now = new Date(), h = now.getHours(), m = now.getMinutes(), s = now
			.getSeconds(), ms = now.getMilliseconds();
	if (h < 10)
		h = "0" + h;
	if (m < 10)
		h = "0" + m;
	if (s < 10)
		h = "0" + s;
	return (h + ":" + m + ":" + s + " " + ms);
}
function getDate() {
	var now = new Date(), y = now.getFullYear(), m = now.getMonth(), w = now
			.getDay(), d = now.getDate();

	if (m < 10)
		h = "0" + m;
	if (d < 10)
		d = "0" + d;
	return (y + "年" + m + "月" + d + "日" + week[w]);
}

function getDateTime() {
	var now = new Date(), yyyy = now.getFullYear(), mm = now.getMonth(), dd = now
			.getDate(), hh = now.getHours(), nn = now.getMinutes(), ss = now
			.getSeconds();

	if (mm < 10)
		mm = "0" + mm;
	if (dd < 10)
		dd = "0" + dd;
	if (hh < 10)
		hh = "0" + hh;
	if (nn < 10)
		nn = "0" + nn;
	if (ss < 10)
		ss = "0" + ss;
	return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + nn + ":" + ss;
}
function menuNode(json) {
	var nodes = [];
	for (i in json) {
		var node = {};
		node.id = json[i].mnuId;
		node.pId = json[i].mnuPid;
		node.name = json[i].mnuName;
		if (json[i].mnuRemark)
			node.title = json[i].mnuRemark;
		else
			node.title = json[i].mnuName;
		node.href = json[i].mnuUrl;
		nodes.push(node);
	}
	return nodes;
}
// 更新系统主题
function themesHandler(item) {
	$('#themes').attr("href",
			"/libs/easyui-1.3.6/themes/" + item.text + "/easyui.css");
	showLog('更换主题');
	$.cookie("skin", item.text, {
		path : "/",
		expires : 30
	});
}
// 用户菜单调用
function menuHandler(item) {
	var match = item.text.match(/.*?ref="(.*?)".*?todo="(.*?)"/im);
	showWindow(match[1], 400, 300);
}

function moduleHandler(item) {
	$.messager.confirm('模块切换确认', '您确认想要切换到其它模块吗？<br>切换模块将关闭当前所有窗口',
			function(r) {
				if (r) {
					var tabs = $('#tt').tabs('tabs');
					for (var i = tabs.length - 1; i > 0; i--) {
						$('#tt').tabs('close', i);
					}
					$('#sysMenu').load('/menu?app=' + item.name);
				}
			});
}
// 系统菜单调用
function onMenuClick(node) {
	var stb = $('#tt').tabs('getTab', node.text);
	if (!stb)
		$('#tt').tabs('add', {
			title : node.text,
			selected : true,
			href : node.url,
			closable : true
		});
	else
		$('#tt').tabs('select', node.text);
}

function showWindow(url, title, width, height) {
	width = width || 500;
	height = height || 400;
	$('#popup').window({
		height : height,
		width : width,
		title : title,
		href : url,
		modal : true
	});
	$('#popup').window('center');
	$('#popup').window('open');
}
function showMoWindow(url, title, width, height) {
	width = width || 500;
	height = height || 400;
	$('#popup').window({
		height : height,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		width : width,
		title : title,
		href : url,
		modal : true
	});
	$('#popup').window('center');
	$('#popup').window('open');
}

$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idField, textField, parentField;
	if (opt.parentField) {
		idField = opt.idField;
		textField = opt.textField;
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idField]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idField] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textField];
				data[i]['title'] = data[i][textField];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textField];
				data[i]['title'] = data[i][textField];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

$.fn.treegrid.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().treegrid.options;
	var idField, textField, parentField;
	if (opt.parentField) {
		idField = opt.idField;
		textField = opt.textField;
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idField]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idField] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textField];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textField];
				data[i]['title'] = data[i][textField];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

$.fn.combotree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idField, textField, parentField;
	if (opt.parentField) {
		idField = opt.idField;
		textField = opt.textField;
		parentField = opt.parentField;

		var i, l, treeData = [], tmpMap = [];

		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idField]] = data[i];
		}

		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idField] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textField];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textField];
				data[i]['title'] = data[i][textField];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

// datagrid右键菜单
function DgContextMenu(e, field) {
	e.preventDefault();
	// 检测mnu+DG名称的菜单是否存在 ,不存就建立
	if ($('#mnu_' + $(this).attr('id')).length == 0) {
		createColumnMenu($(this).attr('id'));
	}

	$('#mnu_' + $(this).attr('id')).menu('show', {
		left : e.pageX,
		top : e.pageY
	});
}
function createColumnMenu(name) {
	$('<div id="mnu_' + name + '"/>').appendTo('body');
	curMenu = $('#mnu_' + name);
	curMenu.menu({
		onClick : function(item) {
			if (item.iconCls == 'icon-ok') {
				$('#' + name).datagrid('hideColumn', item.name);
				curMenu.menu('setIcon', {
					target : item.target,
					iconCls : 'icon-empty'
				});
			} else {
				$('#' + name).datagrid('showColumn', item.name);
				curMenu.menu('setIcon', {
					target : item.target,
					iconCls : 'icon-ok'
				});
			}
		}
	});
	var fields = $('#' + name).datagrid('getColumnFields');
	for (var i = 0; i < fields.length - 1; i++) {
		var field = fields[i];
		var col = $('#' + name).datagrid('getColumnOption', field);
		curMenu.menu('appendItem', {
			text : col.title,
			name : field,
			iconCls : 'icon-ok'
		});
	}
}
