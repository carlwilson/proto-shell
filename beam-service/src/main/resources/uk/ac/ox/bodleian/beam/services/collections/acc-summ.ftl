<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.AccessionsView" -->
<h3>Accessions</h3>
<#if (accessions?size > 1) >
	<p>This collection currently has ${accessions?size} accessions.</p>
	<p>Press the Scan Accessions button to scan them.</p>
<#elseif  (accessions?size > 0) >
	<p>This collection currently has one accession.</p>
	<p>Press the Scan Accessions button to scan it.</p>
<#else>
	<p>This collection currently has no accession directories below it's root.</p>
</#if>
<div class="grid_2">
	<ul id="accessions">
		<#list accessions as acc>
		<li><label>${acc.name?html}</label></li>
		</#list>
	</ul>
	<#if (accessions?size > 0) >
		<button>Scan Accessions</button>
	<#else>
		<p>There are no accessions to scan.</p>
	</#if>
</div>
