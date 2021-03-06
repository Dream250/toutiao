(function (window, undefined) {
    var PopupLogin = Base.getClass('main.component.PopupLogin');
    var PopupUpload = Base.getClass('main.component.SendLetter');
    var Modify = Base.getClass('main.component.Modify');
    var ActionUtil = Base.getClass('main.util.Action');
    var ActionUtil2 = Base.getClass('main.util.Action2');
    Base.ready({
        initialize: fInitialize,
        binds: {
            //.表示class #表示id
            'click .js-sendletter': fClickSendLetter
        },
        events: {
            'click button.click-like': fClickLike,
            'click button.click-dislike': fClickDisLike,
            'click button.click-like2': fClickLike2,
            'click button.click-dislike2': fClickDisLike2
        }
    });

    function fInitialize() {
        if (window.loginpop > 0) {
            fClickLogin();
        }
    }


    function fClickSendLetter() {
        var that = this;
        var username=window.name;
        var localhost=window.localhost;
        //alert("localhost:"+window.localhost);
         /*alert("user:"+window.name);*/
        if(username != localhost) {
            PopupUpload.show({
                listeners: {
                    done: function () {
                        //alert('login');
                        window.location.reload();
                    }
                }
            }, username);
        } else{
            Modify.show({
                listeners: {
                    done: function () {
                        //alert('login');
                        window.location.reload();
                    }
                }
            },localhost);
        }
    }

    function fClickLogin() {
        var that = this;
        PopupLogin.show({
            listeners: {
                login: function () {
                    //alert('login');
                    window.location.reload();
                }
            }
        });
    }

    function fClickLike(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 已经操作过 || 不存在Id || 正在提交 ，则忽略
        if (oEl.hasClass('pressed') || !sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil.like({
            newsId: sId,
            call: function (oResult) {
                oEl.find('span.count').html(oResult.msg);
                oEl.addClass('pressed');
                oEl.parent().find('.click-dislike').removeClass('pressed');
            },
            error: function () {
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

    function fClickDisLike(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 已经操作过 || 不存在Id || 正在提交 ，则忽略
        if (oEl.hasClass('pressed') || !sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil.dislike({
            newsId: sId,
            call: function (oResult) {
                oEl.addClass('pressed');
                var oLikeBtn = oEl.parent().find('.click-like');
                oLikeBtn.removeClass('pressed');
                oLikeBtn.find('span.count').html(oResult.msg);
            },
            error: function () {
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

    function fClickLike2(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 已经操作过 || 不存在Id || 正在提交 ，则忽略
        if (oEl.hasClass('pressed') || !sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil2.like({
            videoId: sId,
            call: function (oResult) {
                oEl.find('span.count').html(oResult.msg);
                oEl.addClass('pressed');
                oEl.parent().find('.click-dislike').removeClass('pressed');
            },
            error: function () {
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

    function fClickDisLike2(oEvent) {
        var that = this;
        var oEl = $(oEvent.currentTarget);
        var sId = $.trim(oEl.attr('data-id'));
        // 已经操作过 || 不存在Id || 正在提交 ，则忽略
        if (oEl.hasClass('pressed') || !sId || that.actioning) {
            return;
        }
        that.actioning = true;
        ActionUtil2.dislike({
            videoId: sId,
            call: function (oResult) {
                oEl.addClass('pressed');
                var oLikeBtn = oEl.parent().find('.click-like');
                oLikeBtn.removeClass('pressed');
                oLikeBtn.find('span.count').html(oResult.msg);
            },
            error: function () {
                alert('出现错误，请重试');
            },
            always: function () {
                that.actioning = false;
            }
        });
    }

})(window);