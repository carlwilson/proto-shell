<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.CollectionsView" -->
<!DOCTYPE html>
<html>
  <#include "/frag/head.html" parse="false">
  <body>
    <#include "/frag/banner.html" parse="false">
    <div id="content" class="container_12">
      <div class="clear"></div>
      <div id="detail" class="grid_8 alpha">
        <h3>Accessions for: ${collection.name}</h3>
        <p>Select an Accession to view in more details or scan.</p>
        <ul id="accessions" class="stylized select">
			<#list collection.accessions as acc>
				<li class="grid_8" onclick="window.location='/beam/collections/${collection.name?url("UTF-8")}/accessions/${acc.details.name?url("UTF-8")}';"}>
						<label>${acc.details.name?html}
							<span class="small">${acc.checked?datetime}</span>
							<span class="small">${acc.details.created?datetime}</span>
						</label>
   				</li>
			</#list>
        </ul>
      </div>
      <div id="summary" class="grid_4 omega">
        <h3>Add a new Accession?</h3>
        <p>These accessions exist below the Collection, but aren't currently recorded.  If you click one you can add it to this collection, but only after it has been hashed checked which may take some time</p>
        <ul id="new-accessions" class="stylized select">
			<#list newAccessions as acc>
				<li class="grid_4" id="${collection.location?url("UTF-8")}acc_${acc.details.name?url("UTF-8")}">
						<label>${acc.details.name?html}
							<span class="small">${acc.checked?datetime}</span>
							<span class="small">${acc.details.created?datetime}</span>
						</label>
						<div class="clear"></div>
						<div class="progressbar">
					      <div></div>
					    </div>
				</li>
			</#list>
        </ul>
      </div>
    </div>
	<#include "/frag/footer.html" parse="false">
	<#include "/frag/modal.html" parse="false">
	<script type="text/javascript">
		$(document).ready(function() {
			selNavbarItem("coll-nav");
	    $.getScript("/scripts/beam-manifests.js", function() {
			checkNewManifests();
	    	});
		});
	</script>
  </body>
</html>
