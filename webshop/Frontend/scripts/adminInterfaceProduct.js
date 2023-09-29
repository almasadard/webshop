let currentPage = 1; // Aktuelle Seite
let allProducts = [];


// Alle Produkte laden

$(document).ready(function () {
    loadProducts();
});

function loadProducts() {
    $.ajax({
        url: "http://localhost:8080/products",
        method: "GET",
        headers: { "Authorization": sessionStorage.getItem("token") },
        success: function (products) {
            allProducts = products;
            createProductTable(products, currentPage);
        },
        error: function (error) {
            console.error(error);
        }
    });
}

//Filter Produkte laden

$(document).on('click', '#filterButton', function () {

    let productTableBody = $("#productTableBody");
    productTableBody.empty();

    const searchterm = document.getElementById('search').value;

    $.ajax({
        type: "GET",
        headers: { "Authorization": sessionStorage.getItem("token") },
        url: "http://localhost:8080/products?searchterm=" + encodeURIComponent(searchterm),
        cors: true,
        success: function (products) {
            allProducts = products; // Alle Produkte aktualisieren
            currentPage = 1;
            createProductTable(products, currentPage)
        },
        error: function (error) { console.error(error) }
    });
});

//Tabelle mit Produkten erzeugen

function createProductTable(products, currentPage) {
    const divPages = $("#pagesButton");
    divPages.show();
    divPages.find("p").text(currentPage);

    const productsPerPage = 6;
    const startIndex = (currentPage - 1) * productsPerPage;
    const endIndex = startIndex + productsPerPage;
    const productsToShow = products.slice(startIndex, endIndex);

    const container = $("#productListContainer");
    container.find("h2").text("Produkt Liste");

    const table = $("<table class='table table-striped mt-3'></table>");
    const thead = $("<thead class='align-middle'><tr></tr></thead>");
    const thead1 = $("<th>ID</th><th>Name</th><th class='text-end pe-2'>Menge</th><th class='text-end pe-3'>Preis</th><th class='text-center'>Active</th>");
    const thead2 = $("<th><div class='input-group input-group-sm ms-4 pe-4'><input class='form-control border-dark' type='search' placeholder='Search' aria-label='Search' id='search'><button class='btn btn-outline-dark bg-light' onclick='filterButton' type='button' id='filterButton'>&#x1F50D;</button></div></th>");
    thead.append(thead1, thead2)

    const tbody = $("<tbody id='productTableBody'></tbody>");

    for (let i = 0; i < productsToShow.length; i++) {
        let product = productsToShow[i];
        let row = $("<tr class='text-'></tr>");
        row.append($("<td class='align-middle'>" + product.id + "</td>"));
        row.append($("<td class='align-middle ps-0'>" + product.name + "</td>"));
        row.append($("<td class='align-middle text-end pe-3'>" + product.quantity + "</td>"));
        row.append($("<td class='align-middle text-end pe-3'>" + product.price.toFixed(2) + " €</td>"));
        row.append($("<td class='align-middle text-center'>" + (product.active ? "&#10004;&#65039;" : "&#10060;") + "</td>"));

        let editButton = $("<button class='btn btn-primary' id='editButton1'>&#x2692;&#xFE0F; Bearbeiten</button>");
        editButton.click(createEditProductHandler(product));


        let deleteButton = $("<button class='btn btn-danger mx-1'>&#x1F5D1;&#xFE0F; Löschen</button>");
        deleteButton.click(createDeleteProductHandler(product.id));

        let buttonCell = $("<td class='text-end'></td>").append(editButton, deleteButton);
        row.append(buttonCell);

        tbody.append(row);


    }

    table.append(thead, tbody);
    container.empty().append("<h2 class='text-center mb-0'>Produktliste</h2>", table);


}

//seiten

$(document).on('click', '#previousPage', function () {
    if (currentPage > 1) {
        currentPage--;
        createProductTable(allProducts, currentPage);
    }
});

$(document).on('click', '#nextPage', function () {
    const totalPages = Math.ceil(allProducts.length / 6);
    if (currentPage < totalPages) {
        currentPage++;
        createProductTable(allProducts, currentPage);
    }
});
// Produkt löschen

function createDeleteProductHandler(productId) {
    return function () {
        if (confirm("Sind sie sicher, dass Sie dieses Produkt löschen möchten?")) {
            $.ajax({
                url: "http://localhost:8080/products/" + productId,
                method: "DELETE",
                headers: { "Authorization": sessionStorage.getItem("token") },
                success: function (response) {
                    console.log("Deleted product:", response);
                    loadProducts();
                },
                error: function (error) {
                    console.error(error);
                }
            });
        }
    };
}

//Produkte bearbeiten (Ansicht)

function createEditProductHandler(product) {
    return function () {
        let productTableBody = $("#productTableBody");
        productTableBody.empty();

        const container = $("#productListContainer");
        container.find("h2").text(`Produkt ID: ${product.id}`);
        container.find("table").remove();

        const divPages = $("#pagesButton");
        divPages.hide();

        let row1 = $("<div class='row'></div>");

        let titleCol = $("<div class='col-9 mb-3 ps-0'></div>");
        let titleLabel = $("<label for='editProductName' class='form-label p-0'>Produktname</label>");
        let titleInput = $("<input type='text' class='form-control' id='editProductName' name='editProductName' value='" + product.name + "'></input>");
        titleCol.append(titleLabel, titleInput)

        let activeCol = $("<div class='col-2 mb-3'></div>");
        let activeLabel = $("<label for='editProductActive' class='form-label p-0'>Active</label>");
        let activeDropdown = $("<select class='form-control' id='editProductActive'></select>");
        activeDropdown.append($("<option value='true'>&#10004;&#65039;</option>"));
        activeDropdown.append($("<option value='false'>&#10060;</option>"));
        activeDropdown.val(product.active.toString());
        activeCol.append(activeLabel, activeDropdown)

        row1.append(titleCol, activeCol);

        let row2 = $("<div class='row mb-3'></div>");

        let col1 = $("<div class='col-6 col-sm-3 mb-2 p-0'></div>");
        let priceLabel = $("<label for='editProductPrice' class='form-label p-0'>Produktpreis</label>");
        let priceInput = $("<div class='input-group'><input type='text' class='form-control' id='editProductPrice' name='editProductPrice' aria-label='Euro amount' value='" + product.price.toFixed(2) + "'><span class='input-group-text'>€</span></div>");
        col1.append(priceLabel, priceInput);

        let col2 = $("<div class='col-6 col-sm-3'></div>");
        let quantityLabel = $("<label for='editProductQuantity' class='form-label p-0'>Produktmenge</label>");
        let quantityInput = $("<input type='number' class='form-control' id='editProductQuantity' name='editProductQuantity' value='" + product.quantity + "'></input>");
        col2.append(quantityLabel, quantityInput);

        let col3 = $("<div class='col p-0'></div>");
        let imgLabel = $("<label for='editProductImage' class='form-label p-0'><a>&#128444;&#65039;</a> Bild ändern</label>");
        let imgInput = $("<input type='file' class='form-control' id='editProductImage' name='editProductImage'>");
        col3.append(imgLabel, imgInput);

        row2.append(col1, col2, col3);

        let row3 = $("<div class='row mb-3'></div>");

        let descriptionLabel = $("<label for='editProductDescription' class='form-label p-0'>Produktbeschreibung</label>");
        let descriptionInput = $("<textarea type='text' class='form-control' id='editProductDescription' name='editProductDescription' rows='3'>" + product.description + "</textarea>");
        row3.append(descriptionLabel, descriptionInput);


        let row4 = $("<div class='row mt-4'></div>");
        let saveCol = $("<div class='col text-end'></div>");
        let saveButton = $("<button class='btn btn-success mx-1'>&#x1F4BE; Speichern</button>");
        saveCol.append(saveButton);
        saveButton.click(createSaveProductHandler(product));

        let cancelCol = $("<div class='col text-start'></div>");
        let cancelButton = $("<button class='btn btn-secondary'>&#x2716;&#xFE0F; Abbrechen</button>");
        cancelCol.append(cancelButton);
        cancelButton.click(function () {
            loadProducts();
        });
        row4.append(saveCol, cancelCol);

        productTableBody.append(row1, row2, row3,row4);

        container.append(productTableBody);
    };
}

//Überarbeitete Produkte der DB hinzufügen

function createSaveProductHandler(product) {
    return function () {

        let productId = product.id;
        let newProductName = document.getElementById("editProductName").value;
        let newProductPrice = parseFloat($("#editProductPrice").val());
        let newProductQuantity = parseInt($("#editProductQuantity").val());
        let newProductImg = product.imageUrl;
        let newProductDescription = $("#editProductDescription").val();
        let productIsActive = $("#editProductActive").val() === "true";

        if (newProductName.trim() === "") {
            newProductName = product.name;
        }
        if (isNaN(newProductPrice)) {
            newProductPrice = product.price;
        }

        if (isNaN(newProductQuantity)) {
            newProductQuantity = product.quantity;
        }
        if (newProductDescription.trim() === "") {
            newProductDescription = product.description;
        }

        let updatedProduct = {
            id: productId,
            name: newProductName,
            price: newProductPrice,
            quantity: newProductQuantity,
            imageUrl: newProductImg,
            description: newProductDescription,
            active: productIsActive,
        };

        console.log("updatedProduct: ", updatedProduct);

        $.ajax({
            url: "http://localhost:8080/products/update",
            method: "PUT",
            headers: { "Authorization": sessionStorage.getItem("token") },
            data: JSON.stringify(updatedProduct),
            contentType: "application/json",
            success: function (response) {
                console.log("Updated product: ", response);

                loadProducts();
            },
            error: function (error) {
                console.error(error);
            }
        });
    };
}