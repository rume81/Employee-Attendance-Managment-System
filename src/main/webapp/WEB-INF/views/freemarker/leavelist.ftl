<table class="table-responsive">
	<#list nod as n>
	<tr>
		<td>
			* ${n!''}
		</td>
	</tr>
	</#list>
</table>
~${nsize?string("0.0")}