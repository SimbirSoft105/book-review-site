<#import "base.ftlh" as base>

<@base.main [] 'Каталог Книг'>
    <section class="contact--title">
        <div class="contact__bg--allFilm"></div>
        <div class="contact--title-inner">
            <div class="contact__inner-title">All Books</div>
            <div class="contact__inner-subtitle">
                <a href="#">Home</a>
                <div class="contact__line">/</div>
                <div>All Books</div>
            </div>
        </div>
    </section>
    <section class="allFilms">
        <div class="allFilms__inner">
            <div class="all__films-filter">
                <div class="filter__tag">
                    <div id="category_top100" class="filter__tag-item">Топ 100</div>
                    <div id="category_fresh" class="filter__tag-item">Свежее</div>
                    <div id="category_what_to_read" class="filter__tag-item">Что почитать</div>
                    <div id="category_most_talked" class="filter__tag-item">Самое обсуждаемое</div>
                </div>
                <form action="" method="post" class="filters">
                    <div class="filter__group">
                        <div class="filter__label">Поиск</div>
                        <div class="filter__field-country">
                            <input id="searchInput" class="search__filter" type="text" name="search"
                                   placeholder="Поиск...">
                        </div>
                    </div>

                    <button id="searchBtn" class="btn__filter" type="button">Применить</button>
                </form>
            </div>
            <div class="all__films-films" id="all__films-films">
                <#assign count = 1>
                <#if books?? && books.content??>
                    <#list books.content as book>
                        <div class="films__item">
                            <div class="films__number">
                                <span class="films__count">${count}</span>
                            </div>
                            <a href="/book/${book.id}" class="film__photo">
                                <#if book.cover??>
                                    <img src="${book.cover}" alt="" class="allfilm__img">
                                <#else>
                                    <img src="/images/default_cover.png" alt="" class="allfilm__img">
                                </#if>
                                <span class="films__img-rating rateItem" title="${book.rate}">${book.rate}</span>
                            </a>
                            <div class="film__info-filter">
                                <a href="/book/${book.id}" class="film__info-left">
                                    <p class="film__info-name">
                                        ${book.title}
                                    </p>
                                    <p class="film__info-date">
                                        ${book.title}, ${book.releaseYear}
                                    </p>
                                    <p class="film__info-metaInfo">
                                        <span class="film__info-country">${book.country.name}</span>
                                        <span class="film__info-genre">
                                    <#list book.categories as category>${category.title}<#sep>, </#list>
                                </span>
                                    </p>
                                </a>
                                <div class="film__info-right"></div>
                            </div>
                        </div>
                        <#assign count++>
                    </#list>
                </#if>
                <div style="display: flex;flex-direction: column;justify-content: center;align-items: center;" id="more_btn" class="block_floating-button">
                    <button style="text-decoration: none;display: inline-block;width: 120px;height: 35px;line-height: 30px;border-radius: 45px;font-family: 'Montserrat', sans-serif;font-size: 11px;text-transform: uppercase;text-align: center;letter-spacing: 1px;font-weight: 600;color: #524f4e;background: white;transition: .3s; cursor: pointer" type="button" class="floating-button">Показать еще
                    </button>
                </div>
                <input id="count" value="${count}" type="hidden">
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. All rights reserved.
            </div>
        </div>
    </footer>

    <script>
        const mainPage = document.querySelector('#page');
        const menu = document.querySelector(".burger");
        const closeBtn = document.querySelector(".close__block");
        const links = document.querySelector("#links");
        const allLinks = document.querySelectorAll(".nav__link-item");
        const header = document.querySelector('#header');
        const btnProfile = document.querySelector('.header__profile--svg');
        const profileContent = document.querySelector('.header__profile-content');

        const allFilterMovies = document.querySelectorAll(".movies__filter-item");
        const openMenu = () => {
            if (links.className.includes("open")) {
                links.classList.remove('open');
                allLinks.forEach((item) => {
                    item.classList.remove("fade");
                });
                mainPage.classList.remove("overflow-hidden");
            } else {
                links.classList.add('open');
                allLinks.forEach((item) => {
                    item.classList.add("fade");
                });
                mainPage.classList.add("overflow-hidden");
            }
        };

        const closeMenu = () => {
            if (links.className.includes("open")) {
                links.classList.remove('open');
                allLinks.forEach((item) => {
                    item.classList.remove("fade");
                });
                mainPage.classList.remove("overflow-hidden");
            }
        };

        const tooltips = () => {
            profileContent.classList.toggle('vis');
        };


        menu.addEventListener("click", openMenu);
        closeBtn.addEventListener("click", closeMenu);
        allLinks.forEach((item) => {
            item.addEventListener("click", closeMenu);
        });
        allFilterMovies.forEach((item) => {
            item.addEventListener("click", function (e) {
                e.preventDefault();
                if (!e.target.getAttribute('class').includes('filter-active')) {
                    allFilterMovies.forEach((i) => {
                        i.classList.remove('filter-active');
                    });
                    this.classList.add('filter-active');
                }
            });
        });
        btnProfile.addEventListener("click", tooltips);


        const allFilterLabel = document.querySelectorAll('.filter__label');
        const allFilterField = document.querySelectorAll('.field__filter');
        const countryItems = document.querySelectorAll('.country-item');
        const genreItems = document.querySelectorAll('.genre-item');
        const yearItems = document.querySelectorAll('.year-item');

        allFilterLabel.forEach(
            (item) => {
                item.addEventListener("click", function (e) {
                    e.preventDefault();
                    if (!e.target.getAttribute('class').includes('act-label')) {
                        this.classList.add('act-label');
                        this.nextElementSibling.classList.add('act-display');
                    } else {
                        this.classList.remove('act-label');
                        this.nextElementSibling.classList.remove('act-display');
                        this.nextElementSibling.firstElementChild.classList.remove('act-filter');
                        this.nextElementSibling.childNodes[3].classList.remove('active__filter-item');
                    }
                });
            });
        allFilterField.forEach(
            (item) => {
                item.addEventListener("click", function (e) {
                    if (!e.target.getAttribute('class').includes('act-filter')) {
                        allFilterField.forEach((i) => {
                            i.classList.remove('act-filter');
                            i.nextElementSibling.classList.remove('active__filter-item');
                        });
                        this.classList.add('act-filter');
                        this.nextElementSibling.classList.add('active__filter-item');
                    } else {
                        this.classList.remove('act-filter');
                        this.nextElementSibling.classList.remove('active__filter-item');
                    }
                })
            });

        countryItems.forEach(
            (item) => {
                item.addEventListener('click', function (e) {
                    e.preventDefault();
                    if (!e.target.getAttribute('class').includes('active__filter-field')) {
                        countryItems.forEach(
                            (i) => {
                                i.classList.remove('active__filter-field');
                            });
                        this.classList.add('active__filter-field');
                        allFilterField.item(0).childNodes[0].textContent = e.target.textContent.trim();
                        allFilterField.item(0).childNodes[1].value = e.target.textContent.trim();
                        allFilterField.item(0).nextElementSibling.classList.remove('active__filter-item');
                        allFilterField.item(0).classList.remove('act-filter');
                    }
                })
            });

        genreItems.forEach(
            (item) => {
                item.addEventListener('click', function (e) {
                    e.preventDefault();
                    if (!e.target.getAttribute('class').includes('active__filter-field')) {
                        genreItems.forEach(
                            (i) => {
                                i.classList.remove('active__filter-field');
                            });
                        this.classList.add('active__filter-field');
                        allFilterField.item(1).childNodes[0].textContent = e.target.textContent.trim();
                        allFilterField.item(1).childNodes[1].value = e.target.textContent.trim();
                        allFilterField.item(1).nextElementSibling.classList.remove('active__filter-item');
                        allFilterField.item(1).classList.remove('act-filter');
                    }
                })
            });

        yearItems.forEach(
            (item) => {
                item.addEventListener('click', function (e) {
                    e.preventDefault();
                    if (!e.target.getAttribute('class').includes('active__filter-field')) {
                        yearItems.forEach(
                            (i) => {
                                i.classList.remove('active__filter-field');
                            });
                        this.classList.add('active__filter-field');
                        allFilterField.item(2).childNodes[0].textContent = e.target.textContent.trim();
                        allFilterField.item(2).childNodes[1].value = e.target.textContent.trim();
                        allFilterField.item(2).nextElementSibling.classList.remove('active__filter-item');
                        allFilterField.item(2).classList.remove('act-filter');
                    }
                })
            });

        let paintRate = () => {
            let rateItems = document.getElementsByClassName("rateItem");
            for (let i = 0; i < rateItems.length; i++) {
                let rate = Number(rateItems[i].title.replace(',', '.'));
                if (rate < 5) {
                    rateItems[i].style.background = 'red';
                } else if (rate < 8 && rate >= 5) {
                    rateItems[i].style.background = 'orange';
                } else {
                    rateItems[i].style.background = 'green';
                }
            }
        };
        paintRate();
    </script>
    <script src="/js/searchAndPaginationForAllBooks.js"></script>
</@base.main>