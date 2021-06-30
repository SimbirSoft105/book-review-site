const fetchHeader =
    {
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        method: 'GET'
    };

async function loadMoreBooks(href, parentNode) {
    let response = await fetch(href, fetchHeader);

    if (!response.ok) return false

    let result = await response.json();
    let bookCount = parentNode.childElementCount;
    let books = await result.content;


    for (let book of books) {
        let bookDiv = document.createElement("div");

        bookCount++;
        bookDiv.innerHTML = getBookHtml(book, bookCount);

        parentNode.insertBefore(bookDiv, parentNode.lastChild)
    }

    return !result.last;
}

function getFormattedCategories(categories) {
    let result = "";

    for (let i = 0; i < categories?.length - 1; i++) {
        result += categories[i] + ", ";
    }

    if (categories?.length > 0) {
        result += categories[categories.length - 1];
    }

    return result;
}

function getBookHtml(book, counter) {
    return `<div class="films__item">
                <div class="films__number">
                    <span class="films__count">${counter}</span>
                </div>
                <a href="/book/${book.id}" class="film__photo">
                    <img src="${book.cover}" alt="" class="allfilm__img">
                    <span class="films__img-rating" title="${book.rate}">${book.rate}</span>
                </a>
                <div class="film__info-filter">
                    <a href="/book/${book.id}" class="film__info-left">
                        <p class="film__info-name">
                            ${book.title}
                        </p>
                        <p class="film__info-date">
                            ${book.releaseYear} год
                        </p>
                        <p class="film__info-metaInfo">
                            <span class="film__info-country">${book.country?.name}</span>
                            <span class="film__info-genre">${getFormattedCategories(book.categories)}</span>
                        </p>
                        <form action="/book/delete/${book.id}" method="post" class="prof__films-delete">
                            <button type="submit" class="btn__delete-films">Удалить книгу</button>
                        </form>
                    </a>
                    <div class="film__info-right"></div>
                </div>
            </div>`;
}
