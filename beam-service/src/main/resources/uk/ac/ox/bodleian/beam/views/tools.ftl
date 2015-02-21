<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.ApplicationView" -->
<!DOCTYPE html>
<!-- The DOCTYPE declaration above will set the    -->
<!-- browser's rendering engine into               -->
<!-- "Standards Mode". Replacing this declaration  -->
<!-- with a "Quirks Mode" doctype may lead to some -->
<!-- differences in layout.                        -->
<html>
  <#include "/frag/head.html" parse="false">
  <body>
    <#include "/frag/banner.html" parse="false">
    <#include "/frag/construction.html" parse="false">
	<#include "/frag/footer.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("tool-nav");
		});
	</script>
  </body>
</html>
