<#-- @ftlvariable name="" type="uk.ac.ox.bodleian.beam.views.CollectionsView" -->
<form id="coll-form" class="stylized">
	<#if newCollection>
		<h3>Enter the New Collection Details</h3>
		<p>All fields are mandatory.</p>
	<#else>
		<h3>Collection: ${collection.details.name?html}</h3>
		<p>Use the Manage Accessions Buttons to scan for new accessions or check the status of existing accessions.</p>
	</#if>
	<fieldset>
		<legend>Collection Details</legend>
		<label for="name" class="grid_2">Name
		<span class="small">Add collection name</span>
		</label>
		<input type="text" id="name" class="grid_5" name="name" placeholder="Collection Name" required maxlength="30" <#if !newCollection> disabled value="${collection.details.name?html}"</#if> />
		<label for="title" class="grid_2">Title
		<span class="small">Add collection title</span>
		</label>
		<input	type="text" id="title" class="grid_5" name="title" placeholder="Collection Title" required maxlength="255" <#if !newCollection> disabled  value="${collection.details.title?html}"</#if>/>
		<label for="root" class="grid_2">Root Dir
		<span class="small">Path to root</span>
		</label>
		<input	type="text" id="root" class="grid_5" name="root" placeholder="Path to Directory" required maxlength="255" <#if !newCollection> disabled  value="${collection.root?html}"</#if>/>
		<#if !newCollection>
			<label for="created" class="grid_2">Date
			<span class="small">Date created</span>
			</label>
			<input	type="text" id="created" class="grid_5" name="created" placeholder="Date Created" required maxlength="255" <#if !newCollection> disabled  value="${collection.details.created?datetime}"</#if>/>
		</#if>
	</fieldset>
	<div class="grid_3 prefix_1">
	<#if newCollection>
			<button type="submit" class="out">Create Collection</button>
	<#else>
			<button type="submit" class="out">Manage Accessions</button>
	</#if>
	</div>
</form>