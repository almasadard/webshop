$.ajax({
    url: "http://localhost:8080/products/active",
    cors: true,
    headers: { "Authorization": sessionStorage.getItem("token") },
    success: function (products) { addProductstoPage(products) },
    error: function (error) { console.error(error) }
});

function addProductstoPage(products) {
    const productsContainer = $("#productsContainer");
    productsContainer.empty();

    let row;
    for (let i = 0; i < products.length; i++) {
        if (i % 3 === 0) {
            row = $(`<div class="row justify-content-center mt-3"></div>`);
            productsContainer.append(row);
        }

        getProductImagePath(products[i].imageUrl, function (file) {
            row.append(createProduct(products[i], file));
        });
    }
}

function getProductImagePath(imageNum, callback) {
    $.ajax({
        url: `http://localhost:8080/files/` + imageNum,
        type: 'GET',
        cors: true,
        headers: { "Authorization": sessionStorage.getItem("token") },
        success: function (file) {
            console.log(file); // Der Dateipfad sollte hier ausgegeben werden
            callback(file); // Rufe das Callback mit dem Dateipfad auf
        },
        error: function (error) {
            console.error('Fehler beim Abrufen des Bildpfads:', error);
        }
    });
}

function createProduct(product, file) {
    const cardContainer = $("<div>", { class: "col-12 col-lg-4 col-xxl-3 d-flex justify-content-center mb-3" });
    const card = $("<div>", { class: "card bg-dark border border-5 border-light text-white p-3", style: "width: 22rem;" });
    const image = $(`<img class="card-img-top border border-1 border-light rounded" height="350" src="${file}">`);
    const cardBody = $(`<div class="card-body border border-1 border-bottom-0 rounded-top-1 mt-1 ">`);
    const name = $(`<h5 class="card-title text-center">${product.name}</h5>`);
    const drop = $(`<div class="d-flex justify-content-center mb-1 mt-2">
    <button type="button" class="btn btn-light" style="width: 1.5rem; height: 1.5rem; padding: 0;"
      data-bs-toggle="modal" data-bs-target="#descriptionModal-${product.id}" data-toggle="tooltip"
      data-placement="top" title="Beschreibung anzeigen">
      <p>&#128220;</p>
    </button>
  </div>`);
    const details = $(`<div class="modal modal-lg fade" id="descriptionModal-${product.id}" tabindex="-1" aria-labelledby="myModalLabel"
  aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content bg-dark text-white">
      <div class="modal-body">
        <div class="row">
          <div class="col-4">
            <div class="d-flex align-items-start h-100">
              <img src="${product.imageUrl}" alt="${product.name}"
                class="img-fluid border border-light">
            </div>
          </div>
          <div class="col-8 d-flex flex-column">
            <div>
              <h3 class="modal-title" id="descriptionModalLabel-${product.id}">${product.name}</h3>
            </div>
            <div class="mt-3">
              <p>${product.description}</p>
            </div>
            <div class="mt-auto">
              <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                  <p class="card-text mb-0">Preis: ${product.price.toFixed(2)} €</p>
                </div>
                <div class="ms-auto">
                  <p class="card-text mb-0">${product.quantity} Stück auf Lager</p>
                </div>
                <div class="ms-3">
                  <button type="button" class="btn btn-light d-flex" id="add-to-cart-button"
                    data-product-id="${product.id}" style="width: 1.5rem; height: 1.5rem; padding: 0;" role="button"
                    data-toggle="tooltip" data-placement="top" title="Produkt zum Warenkorb hinzufügen">
                    <p style="pointer-events: none;">&#x2795;</p>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>`);
    const cardFooter = $(`<div class="card-footer border border-1 border-top-1 d-flex justify-content-between">`);
    const price = $(`<p class="card-text mb-0">Preis: ${product.price.toFixed(2)} €</p>`);
    const addProduct = $(`
  <button type="button" class="btn btn-light d-flex" id="add-to-cart-button" data-product-id="${product.id}" 
    style="width: 1.5rem; height: 1.5rem; padding: 0;" role="button" data-toggle="tooltip" data-placement="top" title="Produkt zum Warenkorb hinzufügen">
    <p style="pointer-events: none;">&#x2795;</p>
  </button>
`);


    cardBody.append(name, drop, details);
    card.append(image, cardBody, cardFooter);
    cardContainer.append(card);
    cardFooter.append(price, addProduct)

    return cardContainer;
}

document.addEventListener("DOMContentLoaded", function () {

    const container = document.getElementById("productsContainer");

    container.addEventListener("click", function (event) {

        if (event.target.matches(".btn-light")) {
            const addToCartButton = event.target;
            const productId = addToCartButton.dataset.productId;
            const quantity = 1;
            addProductToCart(productId, quantity)
        }
    });
});

function addProductToCart(product, quantity) {
    const data = {
        productId: product.id,
        quantity: quantity
    }

    $.post({
        url: "http://localhost:8080/positions",
        headers: { "Authorization": sessionStorage.getItem("token") },
        cors: true,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: console.log,
        error: console.error
    });
}