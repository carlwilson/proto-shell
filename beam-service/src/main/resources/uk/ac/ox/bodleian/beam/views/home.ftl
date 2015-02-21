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
    <div id="content" class="container_12">
      <div class="clear"></div>
      <div id="detail" class="grid_12 alpha">
        <h3>Welcome ${user.name?html}</h3>
        <p>This is the home page for the BEAM system.</p>
      </div>
    </div>
	<#include "/frag/footer.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("home-nav");
		});
	</script>
  </body>
</html>