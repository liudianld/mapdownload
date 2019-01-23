L.drawLocal = {
	draw: {
		toolbar: {
			actions: {
				title: '取消绘图',
				text: '取消'
			},
			undo: {
				title: '删除绘制的最后一个点',
				text: '删除最后一个点'
			},
			buttons: {
				polyline: '画折线',
				polygon: '画多边形',
				rectangle: '画一个矩形',
				circle: '画一个圈',
				marker: '画一个标记'
			}
		},
		handlers: {
			circle: {
				tooltip: {
					start: '单击并拖动绘制圆.'
				},
				radius: 'Radius'
			},
			marker: {
				tooltip: {
					start: '点击地图上放置标记.'
				}
			},
			polygon: {
				tooltip: {
					start: '点击开始绘制.',
					cont: '点击继续绘制.',
					end: '点击第一点,完成绘制.'
				}
			},
			polyline: {
				error: '<strong>错误:</strong> 边缘不能跨越!',
				tooltip: {
					start: '点击开始画线.',
					cont: '点击继续画线.',
					end: '点击最后一个点完成.'
				}
			},
			rectangle: {
				tooltip: {
					start: '单击并拖动绘制矩形.'
				}
			},
			simpleshape: {
				tooltip: {
					end: '松开鼠标完成绘图.'
				}
			}
		}
	},
	edit: {
		toolbar: {
			actions: {
				save: {
					title: '保存更改.',
					text: '保存'
				},
				cancel: {
					title: '取消编辑，放弃所有更改.',
					text: '取消'
				}
			},
			buttons: {
				edit: '编辑图层.',
				editDisabled: '没有层可编辑.',
				remove: '删除图层.',
				removeDisabled: '没有层可删除.'
			}
		},
		handlers: {
			edit: {
				tooltip: {
					text: '拖动手柄，或标记编辑功能.',
					subtext: '单击取消撤消更改.'
				}
			},
			remove: {
				tooltip: {
					text: '点击一个要素删除.'
				}
			}
		}
	}
};