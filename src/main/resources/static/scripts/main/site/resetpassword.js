/**
 * Created by Administrator on 2017/9/24.
 */
(function (window, undefined) {
    var PopupUpload = Base.getClass('main.component.Reset');

    Base.ready({
        initialize: fInitialize,
        binds: {
            //.表示class #表示id
            'click .js-reset': fReset
        }
    });

    function fInitialize() {
        if (window.loginpop > 0) {

        }
    }

    function fReset() {
        var that = this;
        PopupUpload.show({
            listeners: {
                done: function () {
                    window.location.reload();
                }
            }
        });
    }

})(window);