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
    <div id="detail" class="grid_8 alpha">
	    <h3>About Current Session</h3>
	    <span class="small">Details below are gathered from the JVM.</span>
      <form id="envForm" name="envForm" class="stylized">
	    <fieldset>
		  <legend>Session Details</legend>
		  <label for="name" class="grid_2">Name
		    <span class="small">Account name</span>
		  </label>
		  <input type="text" id="name" class="grid_5" name="name" disabled value="${user.name?html}" />
		  <label for="host" class="grid_2">Host
		    <span class="small">Name & IP</span>
		  </label>
		  <input type="text" id="host" class="grid_5" name="host" disabled value="${server.hostName?html} [${server.ipAddress?html}]" />
		  <label for="os" class="grid_2">OS
		    <span class="small">Operating System</span>
		  </label>
		  <input type="text" id="os" class="grid_5" name="os" disabled value="${os.name?html} ${os.version?html} ${os.architecture?html}" />
		  <label for="jvm" class="grid_2">JVM
		    <span class="small">Java Details</span>
		  </label>
		  <input	type="text" id="jvm" class="grid_5" name="jvm" disabled value="${jvm.vendor?html} ${jvm.version?html} ${jvm.architecture?html}" />
	    </fieldset>
      </form>
    </div>
	<#include "/summ/about.html" parse="false">
  </div>
	<#include "/frag/footer.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("about-nav");
		});
	</script>
  </body>
</html>