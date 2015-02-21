<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.ManifestsView" -->
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
        <h3>Beam Manifests</h3>
        <p>This is the contents of the manifests cache.</p>
        <ul id="manifests" class="stylized select">
			<#list cache as manifest>
				<li class="grid_8" onclick="window.location = '/beam/manifests/${manifest.location?url('UTF-8')}';">
						<label>${manifest.location?html}<span>${manifest.totalCount} ${manifest.units}.</span>
						</label>
				</li>
			</#list>
        </ul>
      </div>
    </div>
	<#include "/frag/footer.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("man-nav");
		});
	</script>
  </body>
</html>
