<#import "base.ftlh" as base>

<@base.main [] "Главная страница">
    <#if firstBook??>
        <section class="popular">
            <div class="popular__inner">
                <div class="popular__img">
                    <div class="img-popular" style="background: url('<#if firstBook.cover??>${firstBook.cover}<#else>/images/default_cover.png</#if>') no-repeat center;background-size: cover;"></div>
                </div>
                <h2 class="popular__title">${firstBook.title}</h2>
                <p class="populat__description-short">${firstBook.description}</p>
                <a href="/book/${firstBook.id}" class="popular__link">Read more
                    <svg class="arrow__right-symbol">
                        <use xlink:href="#arrow-right"></use>
                    </svg>
                </a>
            </div>
        </section>
    </#if>

    <#if topBooks?? && topBooks.content??>
        <section class="top__films">
            <#list topBooks.content as topBook>
                <a href="/book/${topBook.id}" class="top__films-item">
                    <#if topBook.cover??>
                        <img src="${topBook.cover}" alt="" class="top__films-img">
                    <#else>
                        <img src="/images/default_cover.png" alt="" class="top__films-img">
                    </#if>
                    <article class="top__films-info">
                        <h3 class="top__films-title">${topBook.title}</h3>
                        <h4 class="top__films-genre"><#list topBook.categories as category>${category.title}<#sep>, </#list></h4>
                        <div class="top__films-link">
                            <div class="block__arrow">
                                <svg class="top__arrow-right--symbol">
                                    <use xlink:href="#arrow-right"></use>
                                </svg>
                            </div>
                            More information
                        </div>
                    </article>
                </a>
            </#list>
        </section>
    </#if>


    <section class="new__movies">
        <div class="new__movies-inner">
            <div class="new__movies-header">
                <h2 class="new__movies-title">ОТКРОЙТЕ НОВЫЕ КНИГИ</h2>
                <p class="new__movies-subtitle">Время найти новинки для чтения</p>
            </div>
            <div class="new__movies-body">
                <#if freshBooks?? && freshBooks.content??>
                    <div class="new__movies-movies">
                        <#list freshBooks.content as freshBook>
                            <div class="new__movies-item">
                                <a href="/book/${freshBook.id}" class="new__movies-img--block">
                                    <#if freshBook.cover??>
                                        <img src="${freshBook.cover}" alt="" class="new__movies-img">
                                    <#else>
                                        <img src="/images/default_cover.png" alt="" class="new__movies-img">
                                    </#if>

                                </a>
                                <a href="/book/${freshBook.id}" class="new__movies-name">${freshBook.title}</a>
                            </div>
                        </#list>
                    </div>
                </#if>
                <div class="view__more-block">
                    <a href="/book/all" class="view__more-btn">Больше книг</a>
                </div>
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

    <script src="/js/header.js"></script>
</@base.main>