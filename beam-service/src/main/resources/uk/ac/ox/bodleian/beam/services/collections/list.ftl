<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.CollectionsView" -->
<h3>Select a Collection to View</h3>
<ul id="collections">
<#list collections as coll>
	<li id="${coll.root}">
		<div>
			<label>${coll.details.name?html}</label>
			<span>${coll.details.title?html}</span>
			<span>${coll.root?html}</span>
			<span>${coll.details.created?datetime}</span>
		</div>
	</li>
</#list>
</ul>
