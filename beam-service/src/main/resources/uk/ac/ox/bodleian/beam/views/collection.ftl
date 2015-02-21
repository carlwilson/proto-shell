<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.CollectionsView" -->
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
		<form id="coll-form" class="stylized">
			<#if collection.name = "new">
				<h3>Enter the New Collection Details</h3>
				<p>All fields are mandatory.</p>
			<#else>
				<h3>Collection: ${collection.name?html}</h3>
				<p>You can still change your mind about the title.</p>
			</#if>
			<fieldset>
				<legend>Collection Details</legend>
				<label for="name" class="grid_2">Name
				<span class="small">Collection name & id</span>
				</label>
				<input type="text" id="name" class="grid_5" name="name" placeholder="Collection Name" required maxlength="100" pattern="[a-zA-Z0-9\-\.]+" <#if !(collection.name = "new")> disabled value="${collection.name?html}"</#if> />
				<label for="title" class="grid_2">Title
				<span class="small">Collection title</span>
				</label>
				<input	type="text" id="title" class="grid_5" name="title" placeholder="Collection Title" required maxlength="255" <#if !(collection.name = "new")> value="${collection.details.title?html}"</#if>/>
				<label for="root" class="grid_2">Location
				<span class="small">Path to collection root</span>
				</label>
				<input	type="text" id="location" class="grid_5" name="location" placeholder="Location of Collection" required maxlength="255" <#if !(collection.name = "new")> disabled  value="${collection.location?html}"</#if>/>
				<label for="created" class="grid_2">Created
				<span class="small">Date collection created</span>
				</label>
				<input	type="text" id="created" class="grid_5" name="created" placeholder="Date Created" required maxlength="255" disabled  value="${collection.details.created?datetime}" />
			</fieldset>
			<div class="grid_3 prefix_1">
				<button type="submit" class="out"><#if (collection.name = "new")>Create<#else>Update</#if> Collection</button>
			</div>
		</form>
      </div>
		<#if (collection.name = "new")>
			<#include "/summ/new-collection.html" parse="false">
		<#else>
			<#include "/summ/coll-accessions.html" parse="true">
		</#if>
    </div>
	<#include "/frag/footer.html" parse="false">
	<#include "/frag/modal.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("coll-nav");
	    	$.getScript("/scripts/beam-collections.js", function() {
			<#if (collection.name = "new")>
				$("#coll-form").submit(function(event) {createCollection(event);});
			<#else>
				$("#coll-form").submit(function(event) {updateCollection(event);});
			</#if>
	    	});
		});
	</script>
  </body>
</html>
