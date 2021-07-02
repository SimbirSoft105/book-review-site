<#import "base.ftlh" as base>

<@base.main>
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <script
                src="https://code.jquery.com/jquery-3.5.1.js"
                integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
                crossorigin="anonymous"></script>
    </head>
    <section class="profile">
        <div class="profile__inner">
            <div class="profile__header">
                <h2 class="profile__title">Ваши рецензии</h2>
                <p class="profile__header-desc">Здесь вы можете просматривать ваши рецензии</p>
            </div>

            <div class="comment__items">
                <#list reviews.content as review>
                    <div id="review#${review.id}" class="comment__item">
                        <div class="comment__desc">
                            <div class="comment__avatar">
                                <a href="#" class="comment__avatar-img">
                                    <img
                                            <#if review.author.avatar??>
                                                src="${review.author.avatar}"
                                            <#else>
                                                src="/images/no_avatar.jpg"
                                            </#if> alt="">
                                </a>
                            </div>
                            <div class="comment__info">
                                <header class="comment__header">
                                    <div style="color: #0e0a0a" class="comment__name">${review.author.name}</div>
                                </header>
                                <p style="color: #0e0a0a" class="comment__text">
                                    ${review.text}
                                </p>
                                <div style="display: flex; flex-direction: row; margin-top: 10px; color: #e0041d">
                                    Оценка: ${review.mark}/10
                                </div>
                            </div>
                            <div class="comment__likes">
                                <button type="button">+</button>
                                <span class="comment__likes-count">${review.rate}</span>
                                <button type="button">-</button>
                            </div>
                        </div>
                        <div class="comment__footer">
                            <a href="/reviews/${review.id}/delete" class="answer__btn__">Удалить</a>
                            <div class="comment__date">${review.createdAt}</div>
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
        let loadMore = true
        let currentPage = 0;
        let reviewsBlock = document.querySelector('.comment__items')

        window.onscroll = async function () {
            // console.log('currentPage: ' + currentPage)
            // console.log('load more: ' + loadMore)
            if (loadMore &&

                window.scrollY + 1 >= document.documentElement.scrollHeight - document.documentElement.clientHeight) {

                let response = await fetch('/reviews/my/ajax?page=' + (currentPage + 1))
                if (response.ok) {

                    let result = await response.json()
                    let reviews = result.content
                    for (let review of reviews) {

                        let avatar
                        if (review.author.avatar != null) {
                            avatar = review.author.avatar
                        } else avatar = '/images/no_avatar.jpg'
                        let name = review.author.name
                        let text = review.text
                        let id = review.id
                        let rate = review.rate
                        let mark = review.mark
                        let createdAt = review.createdAt

                        let reviewBlock = document.createElement('div')
                        reviewBlock.innerHTML = `<div id="review#` + id + `" class="comment__item">
                            <div class="comment__desc">
                                <div class="comment__avatar">
                                    <a href="#" class="comment__avatar-img">
                                        <img src="` + avatar + `" alt=""/>
                                    </a>
                                </div>
                                <div class="comment__info">
                                    <header class="comment__header">
                                        <div style="color: #0e0a0a" class="comment__name">` + name + `</div>
                                    </header>
                                    <p style="color: #0e0a0a" class="comment__text">`
                            + text + `
                                    </p>
                                    <div style="display: flex; flex-direction: row; margin-top: 10px; color: #e0041d">
                                        Оценка: ` + mark + `/10
                                    </div>
                                </div>
                                <div class="comment__likes">
                                    <button type="button">+</button>
                                    <span class="comment__likes-count">` + rate + `</span>
                                    <button type="button">-</button>
                                </div>
                            </div>
                            <div class="comment__footer">
                                <a href="/reviews/` + id + `/delete" class="answer__btn__">Удалить</a>
                                <div class="comment__date">` + createdAt + `</div>
                            </div>
                        </div>`

                        reviewsBlock.appendChild(reviewBlock)
                    }
                    loadMore = result.last === false;
                    console.log('last: ' + result.last)
                    currentPage += 1
                }
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