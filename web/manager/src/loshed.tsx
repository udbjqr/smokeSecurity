import Vue,{ CreateElement } from 'vue';
Vue.prototype.$header = (h:CreateElement,{ column, $index }:any):any => {
	const hk = "kk"+ $index
  const ff = "ff"+ $index
	return (
		<div class="cell">
			{column.label}
			<span class="caret-wrapper" id={hk} style="display:none">
				<i class="sort-caret ascending"></i>
				<i class="sort-caret descending"></i>
			</span>
			<span id={ff}></span>
		</div>
	)
}