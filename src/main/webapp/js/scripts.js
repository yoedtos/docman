function getActualYear() {
	var year = new Date().getFullYear();
	document.write(year);
}

function openDropModal(dropId) {
	document.getElementById('id').value = dropId;
	document.getElementById('dropModal').style.display='block';
}

function closeDropModal() {
	document.getElementById('dropModal').style.display='none';
}

function validateFields() {
	var isValid = true;
	var message = "The text size is too big!";
	
	var title = document.getElementById('title');
	if(title.value.length > 50) {
		title.style.color="red";
		document.getElementById('titleError').innerHTML = message;
		isValid = false;
	} 
	
	var author = document.getElementById('author');	
	if(author.value.length > 30) {
		author.style.color="red";
		document.getElementById('authorError').innerHTML = message;
		isValid = false;
	}
	
	var desc = document.getElementById('description');
	if(desc.value.length > 300) {
		desc.style.color="red";
		document.getElementById('descriptionError').innerHTML = message;
		isValid = false;
	}
	
	var tags = document.getElementById('tags');
	if(tags.value.length > 100) {
		tags.style.color="red";
		document.getElementById('tagsError').innerHTML = message;
		isValid = false;
	}
	
	return isValid;
}