<!DOCTYPE html>
<html>
<head><title>Aplicación notas</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">

</head>
<body class="container" style="background-color: #649CC8">
<h1 class="mt-5">Proyecto SecDevOps</h1>
<h2 class="text-center font-weight-bold text-warning">App de Alberto</h2>
<form action="/tarea" method="POST" enctype="multipart/form-data">
  <h4 class="mt-4">Escribe una nota</h4>
  <div class="col">
     <textarea class="mt-4 col-sm-8" rows="4"
            name="description"><#if description??>${description}</#if>
     </textarea>
     <div class="row mt-4 mb-4">
       <button class=" ml-4 btn btn-warning col-sm-2" type="submit"
     	  name="publish">Publicar Nota
       </button>
       
      </div>
 </div>
</form>
<h2>Lista de Notas</h2>
<div class="container">
 <#if tareas??>
        <#list tareas as id, description>
        <form  action="/delete" method="POST" enctype="multipart/form-data">
			<div class="row">
			  <textarea style="visibility: hidden; position: absolute;" rows="1"
            name="tarea">${id}
     </textarea>
 				<div class="rounded border border-danger col-sm-10 bg-warning mt-2">
      				<p>${description}</p>
    			</div>
    			<div class="col-sm-2 mt-2">
      				<button class=" mt-2 btn btn-danger" type="submit" name="delete">Eliminar</button>
    			</div>
			</div>
			</form>
		<#else>
            <p class="lh-copy f6">No tienes notas aún.</p>
       	</#list>
    </#if>
</div>

</body>
</html>