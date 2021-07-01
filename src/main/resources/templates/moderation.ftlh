<#import "base.ftlh" as base>

<@base.main [] "Страница модерации">
    <section class="profile">
        <div class="profile__inner">
            <div class="profile__header">
                <h2 class="profile__title">Страница модерации</h2>
                <p class="profile__header-desc"></p>
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
                            </a>
                            <div class="film__info-right">
                                <form action="/book/delete/${book.id}" method="post" class="prof__films-delete">
                                    <button type="submit" class="btn__delete-films">Удалить книгу</button>
                                </form>
                                <form action="/book/delete/${book.id}" method="post" class="prof__films-delete">
                                    <button type="submit" class="btn__delete-films">Удалить книгу</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. Все права защищены.
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

                loadMoreBooks('/book/rest/user/${user.id}?page=' + currentPage + '&size=7&sort=id,desc', parent)
                    .then(haveMore => {
                        loadMore = haveMore;
                        paintRate(rateItems);
                    });

                currentPage += 1;
            }
        }

        paintRate(rateItems);
    </script>

    <script src="/js/header.js"></script>
</@base.main>