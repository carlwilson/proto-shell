<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.CollectionsView" -->
<!DOCTYPE html>
<html>
  <#include "/frag/head.html" parse="false">
  <body>
    <#include "/frag/banner.html" parse="false">
    <div id="content" class="container_12">
      <div class="clear"></div>
      <div id="detail" class="grid_8 alpha">
        <h3>Beam Collections</h3>
        <p>Please select a Collection to view in more detail, or manage.</p>
        <ul id="collections" class="stylized select">
			<#list collections as coll>
				<li class="grid_8" onclick="window.location='/beam/collections/${coll.name?url("UTF-8")}';"}>
						<label>${coll.name?html}
							<span class="small">${coll.details.title?html}</span>
							<span class="small">${coll.location?html}</span>
							<span class="small">${coll.details.created?datetime}</span>
						</label>
				</li>
			</#list>
        </ul>
      </div>
      <div id="summary" class="grid_4 omega">
        <h3>Create a new Collection?</h3>
        <p>If you wish to create another collection please press the anchor below.</p>
        <div class="grid_2 prefix_1">
        <a href="/beam/collections/new">Create Collection</a>
        </div>
      </div>
    </div>
	<#include "/frag/footer.html" parse="false">
	<#include "/frag/modal.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("coll-nav");
		});
	</script>
  </body>
</html>
