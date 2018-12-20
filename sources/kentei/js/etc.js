$(document).ready(function(){
	$('.hitable tr').hover(
		function(){
			$(this).addClass('hv');
		},
		function(){
			$(this).removeClass('hv');
		}
	);
});