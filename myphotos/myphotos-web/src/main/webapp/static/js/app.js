$(function () {
    initMenu();
    initLoadMoreButton();
    initPoptrox();
    initSortModeSelector();
    initGooglePlusSignUp();
    initDataBindId();
    initAvatarUploader();
    initPhotosUploader();

    function showError(msg) {
        if (msg === undefined) {
            alert(messages.error);
        } else {
            alert(msg);
        }
    }

    function initMenu() {
        $('.menu-btn').click(function () {
            var items = $(this).parent().find('.items');
            if (items.hasClass('show-menu')) {
                items.removeClass('show-menu');
            } else {
                items.addClass('show-menu');
            }
        });
    }

    function initSortModeSelector() {
        $('#sort-mode-selector').on('change', function () {
            var option = this.value;
            window.location = 'home.html?sort=' + option;
        });
    }

    function initPoptrox() {
        $('#photo-container').poptrox({
            caption: function ($a) {
                return $a.next('figcaption').html();
            },
            overlayColor: '#2c2c2c',
            overlayOpacity: 0.85,
            popupCloserText: '',
            popupLoaderText: '',
            selector: '.photo-item.not-init a.image',
            usePopupCaption: true,
            usePopupDefaultStyling: false,
            usePopupEasyClose: true,
            usePopupNav: true,
            windowMargin: (skel.breakpoint('small').active ? 0 : 50)
        });
        $('.photo-item.not-init').removeClass('not-init');
    }

    function showLoadingIndicator() {
        //https://loading.io/
        var btn = $('#load-more-button');
        btn.addClass('hidden');
        btn.parent().append('<img src="static/images/loading.gif" alt="Loading..." class="loading-indicator">');
    }

    function hideLoadingIndicator() {
        var btn = $('#load-more-button');
        btn.parent().find('img').remove();
        btn.removeClass('hidden');
    }

    function initLoadMoreButton() {
        $('#load-more-button').click(function () {
            var c = $('#photo-container');
            var page = parseInt(c.attr('data-page')) + 1;
            var photoCount = parseInt(c.attr('data-total-count'));
            var url = c.attr('data-more-url') + '&page=' + page;
            showLoadingIndicator();
            $.ajax({
                url: url,
                contentType: 'text/html',
                success: function (data) {
                    hideLoadingIndicator();
                    c.append(data);
                    c.attr('data-page', page);
                    initPoptrox();
                    if (photoCount <= c.find('.photo-item').length) {
                        $('#load-more-container').remove();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    hideLoadingIndicator();
                    showError();
                }
            })
        });
    }

    function initGooglePlusSignUp() {
        var button = $('[data-sign-up]');
        if (button.length > 0) {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: googlePlusClientId,
                    cookiepolicy: 'profile email',
                });
                auth2.attachClickHandler(button.get()[0], {}, function (googleUser) {
                    var authToken = googleUser.getAuthResponse().id_token;
                    var auth2 = gapi.auth2.getAuthInstance();
                    auth2.signOut().then(function () {
                        window.location = '/from/google-plus?code=' + authToken;
                    });
                }, function (error) {
                    console.log(JSON.stringify(error, undefined, 2));
                    defaultErrorHandler();
                });
                console.log("Init google plus signup successful");
            });
        }
    }

    function initDataBindId() {
        $.each($('[data-bind-id]'), function (index, val) {
            var el = $(val);
            el.on('input', function (e) {
                var value = $(this).val();
                var bindId = $(this).attr('data-bind-id');
                $('#' + bindId).text(value);
            });
        });
    }
    //https://tellibus.com/fineuploader/fine-uploader-basic-demo.html
    function initAvatarUploader() {
        var avatarUploader = $('#avatar-uploader');
        if (avatarUploader.length > 0) {
            new qq.FineUploaderBasic({
                button: avatarUploader[0],
                request: {
                    endpoint: 'upload-avatar.json'
                },
                validation: {
                    allowedExtensions: ['jpeg', 'jpg', 'gif', 'png']
                },
                callbacks: {
                    onSubmit: function (id, fileName) {
                        $('#avatar-uploader').append('<span class="background progress"></span><img src="/static/images/loading.gif" alt="Loading..." class="progress avatar-uploading">');
                    },
                    onComplete: function (id, fileName, responseJSON) {
                        $('#avatar-uploader .progress').remove();
                        if (responseJSON.success) {
                            $('#avatar-uploader img').attr('src', responseJSON.thumbnailUrl);
                        } else {
                            showError(responseJSON.error);
                        }
                    }
                },
                debug: true
            });
        }
    }

    function initPhotosUploader() {
        var upload = $('#upload-photo .button');
        if (upload.length > 0) {
            new qq.FineUploaderBasic({
                button: upload[0],
                request: {
                    endpoint: 'upload-photos.json'
                },
                validation: {
                    allowedExtensions: ['jpeg', 'jpg', 'gif', 'png']
                },
                callbacks: {
                    onSubmit: function (id, fileName) {
                        upload.hide();
                        $('#upload-photo .upload-container').append('<img src="/static/images/loading.gif" alt="Loading..." class="progress photo-uploading">');
                    },
                    onComplete: function (id, fileName, responseJSON) {
                        upload.show();
                        $('#upload-photo .progress').remove();
                        if (responseJSON.success) {
                            addPhotoUploadResult(responseJSON);
                        } else {
                            showError(responseJSON.error);
                        }
                    }
                },
                debug: true
            });
        }
    }

    function addPhotoUploadResult(result) {
        var template = $('#upload-photo-result-template').html();
        $('#upload-photo').after(template);
        $('#current-photo .large-photo').attr('href', result.largeUrl);
        $('#current-photo .large-photo').removeClass('large-photo');
        $('#current-photo .small-photo').attr('src', result.smallUrl);
        $('#current-photo .small-photo').removeClass('small-photo');
        $('#current-photo .original-photo').attr('href', result.originalUrl);
        $('#current-photo .original-photo').removeClass('original-photo');

        $('#current-photo .created').after(result.created);
        $('#current-photo .created').remove();
        $('#current-photo .views').after(result.views);
        $('#current-photo .views').remove();
        $('#current-photo .downloads').after(result.downloads);
        $('#current-photo .downloads').remove();
        $('#current-photo').removeAttr('id');
        
        var photoLimit = parseInt($('#photo-container').attr('data-photo-limit'));
        var realItems = $('#photo-container .photo-item').length;
        var toRemoveCount = realItems - photoLimit;
        while(toRemoveCount > 0) {
            $('#photo-container .photo-item:last').remove();
            toRemoveCount--;
        }
		recreateAdaptiveClasses();
        initPoptrox();
    }
	
	function recreateAdaptiveClasses(){
		var i;
		var photoItems = $('#photo-container .photo-item');
		for(i = 0; i < photoItems.length; i++) {
			var item = $(photoItems[i]);
			item.removeClass('6u');
			item.removeClass('6u$');
			if(i % 2 == 0) {
				item.addClass('6u');
			} else {
				item.addClass('6u$');
			}
		}
	}
});
