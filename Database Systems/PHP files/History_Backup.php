<!DOCTYPE>
<html>
<head>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/js/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="/js/jqplot.dateAxisRenderer.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/jquery.jqplot.css" />
<script>
	function pm_graph(line1, line2, line3) {
		var plot3 = $.jqplot('chart3div', [ line1, line2, line3 ], {
			title : 'Physical Mood/Mental Mood/Sleep Event History',
			axes : {
				xaxis : {
					label : 'Timestamp',
					renderer : $.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%b %#d, %#I %p'
					},
					tickInterval : '2 days'
				},
				yaxis : {
					label : 'Mood Condition/Sleep Duration',
					tickInterval : '1'
				}
			},
			legend : {
				show : true,
				renderer : $.jqplot.EnhancedLegendRenderer,
				location : 's',
				labels : [ 'Physical Mood: 1 - Exhausted, 2 - Tired, 3 - Normal, 4 - Active, 5 - Energetic','Mental Mood: 1 - Upset, 2 - Sad, 3 - Normal, 4 - Happy, 5 - Excited','Sleep Event' ],
				placement : "outside",
				marginTop : "30px",
				rendererOptions : {
					numberRows : 5
				}
			},
			series : [ {
				lineWidth : 4,
				markerOptions : {
					style : 'square'
				}
			} ]
		});
	}


	function phy_mood_graph(line1) {
		var plot3 = $.jqplot('chart1div', [ line1 ], {
			title : 'Physical Mood History',
			axes : {
				xaxis : {
					label : 'Timestamp',
					renderer : $.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%b %#d, %#I %p'
					},
					tickInterval : '2 days'
				},
				yaxis : {
					label : 'Mood Condition',
					min : 1.0,
					tickInterval : '1'
				}
			},
			legend : {
				show : true,
				renderer : $.jqplot.EnhancedLegendRenderer,
				location : 'ne',
				labels : [ '1 - Exhausted, 2 - Tired, 3 - Normal, 4 - Active, 5 - Energetic' ],
				placement : "outside",
				marginTop : "30px",
				rendererOptions : {
					numberRows : 5
				}
			},
			series : [ {
				lineWidth : 4,
				markerOptions : {
					style : 'square'
				}
			} ]
		});
	}
	
	function mental_mood_graph(line1) {
		var plot3 = $.jqplot('chart2div', [ line1 ], {
			title : 'Mental Mood History',
			axes : {
				xaxis : {
					label : 'Timestamp',
					renderer : $.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%b %#d, %#I %p',
					},
					tickInterval : '2 days'
				},
				yaxis : {
					label : 'Mood Condition',
					min : 1.0,
					max : 5.0,
					tickInterval : '1'
				}
			},
			legend : {
				show : true,
				renderer : $.jqplot.EnhancedLegendRenderer,
				location : 'ne',
				labels : [ '1 - Upset, 2 - Sad, 3 - Normal, 4 - Happy, 5 - Excited' ],
				placement : "outside",
				marginTop : "30px",
				rendererOptions : {
					numberRows : 5
				}
			},
			series : [ {
				lineWidth : 4,
				markerOptions : {
					style : 'square'
				}
			} ]
		});
	}
	function food_event_graph(line1) {
		var plot3 = $.jqplot('chartdiv', [ line1 ], {
			title : 'Food History',
			axes : {
				xaxis : {
					label : 'Timestamp',
					renderer : $.jqplot.DateAxisRenderer,
					tickOptions : {
						formatString : '%b %#d, %#I %p',
					},
					tickInterval : '6 days'
				},
				yaxis : {
					label : 'Food Item',
					min : 1.0,
					max : 6.0,
					tickInterval : '1'
				}
			},
			legend : {
				show : true,
				renderer : $.jqplot.EnhancedLegendRenderer,
				location : 'ne',
				labels : [ '1 - Dairy, 2 - Dessert, 3 - Fruit, 4 - Grain, 5 - Meat, 6 - Vegetable' ],
				placement : "outside",
				marginTop : "30px",
				rendererOptions : {
					numberRows : 5
				}
			},
			series : [ {
				lineWidth : 4,
				markerOptions : {
					style : 'square'
				}
			} ]
		});
	}
</script>
</head>
<!-- <body background="/images/bg.jpg">
	<div id="chartdiv" style="height: 400px; width: 400px;"></div>
</body> -->
<body>
<div id="chart3div" style="height: 400px; width: 1500px;"></div>
<br>
<br>
<div id="chartdiv" style="height: 400px; width: 900px;"></div>
<br>
<br>
<div id="chart1div" style="height: 400px; width: 1500px;"></div>
<br>
<br>
<div id="chart2div" style="height: 400px; width: 1500px;"></div>
<?php 
include("DB.php");
$userid = $_GET['user_id'];
$query = "SELECT date(time_stamp) as p_stamp, avg(mood_condition) as p_condition from physical_mood where user_id='$userid' and date(time_stamp) between '2015-11-01' and '2015-11-30' group by date(time_stamp) order by date(time_stamp);";
$cat = mysql_query($query) or die(mysql_error());
$var = '[';
while($row = mysql_fetch_array($cat))
{
$var = $var.'['.'\'';
$var = $var.$row['p_stamp'].'\'';
$var = $var.',';
$var = $var.$row['p_condition'];
$var = $var.'],';
}
$var = $var.']';
$var = str_replace(",]","]",$var);
echo '<script type="text/javascript">phy_mood_graph('.$var.');</script>';
$query1 = "SELECT date(time_stamp) as m_stamp, avg(mood_condition) as m_condition from mental_mood where user_id='$userid' and date(time_stamp) between '2015-11-01' and '2015-11-30' group by date(time_stamp) order by date(time_stamp);";
$cat1 = mysql_query($query1) or die(mysql_error());
$var1 = '[';
while($row1 = mysql_fetch_array($cat1))
{
$var1 = $var1.'['.'\'';
$var1 = $var1.$row1['m_stamp'].'\'';
$var1 = $var1.',';
$var1 = $var1.$row1['m_condition'];
$var1 = $var1.'],';
}
$var1 = $var1.']';
$var1 = str_replace(",]","]",$var1);
echo '<script type="text/javascript">mental_mood_graph('.$var1.');</script>';

$query2 = "SELECT time_stamp, category from food_event where user_id='$userid';";
$cat2 = mysql_query($query2) or die(mysql_error());
$var2 = '[';
while($row2 = mysql_fetch_array($cat2))
{
$var2 = $var2.'['.'\'';
$var2 = $var2.$row2['time_stamp'].'\'';
$var2 = $var2.',';
$var2 = $var2.$row2['category'];
$var2 = $var2.'],';
}
$var2 = $var2.']';
$var2 = str_replace(",]","]",$var2);
$var2 = str_replace("Dairy", "1", $var2);
$var2 = str_replace("Dessert", "2", $var2);
$var2 = str_replace("Fruit", "3", $var2);
$var2 = str_replace("Grain", "4", $var2);
$var2 = str_replace("Meat", "5", $var2);
$var2 = str_replace("Vegetable", "6", $var2);
echo '<script type="text/javascript">food_event_graph('.$var2.');</script>';

$query3 = "select date(p.time_stamp) as p_stamp, avg(p.mood_condition) as p_condition, date(m.time_stamp) as m_stamp, avg(m.mood_condition) as m_condition, date(s.start_time) as s_stamp, avg(s.duration) as s_duration from physical_mood p, mental_mood m, sleep_event s where p.user_id = m.user_id and m.user_id = s.user_id and date(p.time_stamp) = date(m.time_stamp) and date(m.time_stamp) = date(s.start_time) and p.user_id = 5 and p.time_stamp between '2015-11-01 00:00:00' and '2015-11-30 00:00:00' group by date(p.time_stamp) order by date(p.time_stamp);";
$cat3 = mysql_query($query3) or die(mysql_error());
$var3 = '[';
$var4 = '[';
$var5 = '[';
while($row3 = mysql_fetch_array($cat3))
{
$var3 = $var3.'['.'\'';
$var3 = $var3.$row3['p_stamp'].'\'';
$var3 = $var3.',';
$var3 = $var3.$row3['p_condition'];
$var3 = $var3.'],';

$var4 = $var4.'['.'\'';
$var4 = $var4.$row3['m_stamp'].'\'';
$var4 = $var4.',';
$var4 = $var4.$row3['m_condition'];
$var4 = $var4.'],';

$var5 = $var5.'['.'\'';
$var5 = $var5.$row3['s_stamp'].'\'';
$var5 = $var5.',';
$var5 = $var5.$row3['s_duration'];
$var5 = $var5.'],';
}
$var3 = $var3.']';
$var4 = $var4.']';
$var5 = $var5.']';
$var3 = str_replace(",]","]",$var3);
$var4 = str_replace(",]","]",$var4);
$var5 = str_replace(",]","]",$var5);
echo '<script type="text/javascript">pm_graph('.$var3.','.$var4.','.$var5.');</script>';
?>
</body>
</html>