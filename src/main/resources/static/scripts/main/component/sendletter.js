/**
var oPopupUpload = new PopupUpload({
    
});
 */
(function (window) {
    var PopupUpload = Base.createClass('main.component.SendLetter');
    var Popup = Base.getClass('main.component.Popup');
    var Upload = Base.getClass('main.component.Upload');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');


    Base.mix(PopupUpload, Component, {
        _tpl: [
            '<div>',
              '<div class="form-group">',
                '<div class="form-group">',
                    '<label class="col-sm-2 control-label">收信人</label>',
                    '<label  class="js-name" id="name"></label>',
                    '<script>',
                        'var value = username;',
                        'document.getElementById("name").innerHTML=value',
                    '</script>',
                '</div>',
                  '<div class="form-group"><label class="col-sm-2 control-label">发送内容</label><div class="col-sm-10"> <textarea rows="5" class="js-content form-control" type="text" placeholder="请输入内容~" id="content"></textarea></div></div>',
                '<div class="form-group">',
                    '<div class="col-input-login">',
                        '<a class="btn btn-info js-send" href="javascript:void(0);">发送</a>',
                        '<a class="btn btn-info js-cancel" href="javascript:void(0);">取消</a>',
                    '</div>',
                '</div>',
            '</div>'].join(''),
        listeners: [{
            name: 'render',
            type: 'custom',
            handler: function () {
                var that = this;
                var oEl = that.getEl();
            }
        }, {
            name: 'click a.js-send',
            handler: function () {
                var that = this;
                var oEl = that.getEl();
                var sName = $.trim(oEl.find('label.js-name').val());
                var sContent = $.trim(oEl.find('textarea.js-content').val());
                sContent=sContent.replace(/\r\n/g, '<br/>').replace(/\n/g, '<br/>').replace(/\s/g, ' ');
                if (!sContent) {
                    return alert('内容不能为空');
                }
                if (that.requesting) {
                    return;
                }
                that.requesting = true;
                $.ajax({
                    url: '/user/addNews/',
                    method: 'post',
                    data: { title: sName, link: sContent},
                    dataType: 'json'
                }).done(function (oResult) {
                    that.emit('done');
                }).fail(function (oResult) {
                    alert('出现错误，请重试');
                }).always(function () {
                    that.requesting = false;
                });
            }
        },{
            name: 'click a.js-cancel',
            handler: function () {
                var that = this;
                document.getElementById("content").value="";
            }
        }],
        show: fStaticShow
    }, {
        initialize: fInitialize
    });

    function fStaticShow(oConf,username) {
        var that = this;
        window.username = username;
        var oLogin = new PopupUpload(oConf);
        var oPopup = new Popup({
            title: '发送私信',
            width: 700,
            content: oLogin.html()
        });
        oLogin._popup = oPopup;
        Component.setEvents();
    }

    function fInitialize(oConf) {
        var that = this;
        delete oConf.renderTo;
        PopupUpload.superClass.initialize.apply(that, arguments);
    }

})(window);