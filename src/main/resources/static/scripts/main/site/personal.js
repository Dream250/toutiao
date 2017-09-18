(function (window, undefined) {
    var PopupLogin = Base.getClass('main.component.PopupLogin');
    var PopupUpload = Base.getClass('main.component.SendLetter');

    Base.ready({
        initialize: fInitialize,
        binds: {
            //.表示class #表示id
            'click .js-sendletter': fClickSendLetter
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
         /*alert("user:"+window.name);*/
        PopupUpload.show({
            listeners: {
                done: function () {
                    //alert('login');
                    window.location.reload();
                }
            }
        },username);
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

})(window);