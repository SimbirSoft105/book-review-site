<#import "base.ftlh" as base>

<@base.main [] "Избранное">
    <section class="profile">
        <div class="profile__inner">
            <div class="profile__header">
                <h2 class="profile__title">Избранное</h2>
                <p class="profile__header-desc">Здесь вы можете просматривать ваши избранные книги</p>
            </div>

            <div class="all__films-films">
                <#list books.content as book>
                    <div class="films__item">
                        <div class="films__number">
                            <span class="films__count">${book?counter}</span>
                        </div>
                        <a href="/book/${book.id}" class="film__photo">
                            <#if book.cover??>
                                <img src="${book.cover}" alt="" class="allfilm__img">
                            <#else>
                                <img src="/images/default_cover.png" alt="" class="allfilm__img">
                            </#if>
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
                                    <#if book.country??>
                                        <span class="film__info-country">${book.country.name}</span>
                                    </#if>
                                    <span class="film__info-genre">
                                        <#list book.categories as category>${category.title}<#sep>, </#list>
                                    </span>
                                </p>
                                <form action="/book/favorites/delete/${book.id}" method="post" class="prof__films-delete">
                                    <button type="submit" class="btn__delete-films">Удалить книгу</button>
                                </form>
                            </a>
                            <div class="film__info-right"></div>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo-1x.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. All rights reserved.
            </div>
        </div>
    </footer>

    <script src="/js/showMoreBook.js"></script>
    <script src="/js/raiteColor.js"></script>

    <script>
        let rateItems = document.getElementsByClassName("films__img-rating")
        let parent = document.querySelector(".all__films-films");
        let loadMore = true;
        let currentPage = 1;

        window.onscroll = function () {
            if (loadMore &&
                window.scrollY + 1 >= document.documentElement.scrollHeight - document.documentElement.clientHeight) {

                loadMore = false;

                loadMoreBooks('/book/rest/favorites/${user.id}?page=' + currentPage + '&size=7&sort=id,desc', parent)
                    .then(haveMore => {
                        loadMore = haveMore;
                        paintRate(rateItems);
                    });

                currentPage += 1;
            }
        }

        paintRate(rateItems);
    </script>

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
    </script>
</@base.main>