(function (window) {
    var PopupReigster = Base.createClass('main.component.PopupReigster');
    var Popup = Base.getClass('main.component.Popup');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');

    Base.mix(PopupReigster, Component, {
        _tpl: [
            '<div class="wrapper-content clearfix">',
                '<div class="input-section">',
                    '<div class="form-group">',
                        '<label class="control-label">用户名</label>',
                        '<div class="control-group js-username2"><input type="text" placeholder="请输入用户名"></div>',
                    '</div>',
                    '<div class="form-group">',
                        '<label class="control-label">密码</label>',
                        '<div class="control-group js-pwd2"><input type="password" placeholder="请输入密码"></div>',
                    '</div>',

                '<div class="form-group">',
                    '<label class="control-label">邮箱</label>',
                    '<div class="control-group js-email2"><input type="email" placeholder="请输入邮箱"></div>',
                '</div>',
                   /* '<div class="form-group about-pwd">',
                        '<div class="keep-pwd">',
                            '<label><input type="checkbox"  class="js-rember"> 记住登录</label>',
                        '</div>',
                    '</div>',*/
                    '<div class="form-group">',
                        '<div class="col-input-login">',
                            /*'<a class="btn btn-info js-login" href="javascript:void(0);">登录</a>',*/
                            '<a class="btn btn-info js-register" href="javascript:void(0);">注册</a>',
                        '</div>',
                    '</div>',
                '</div>',
            '</div>'
        ].join(''),
        listeners: [{
                    name: 'render',
                    type: 'custom',
                    handler: function () {
                        var that = this;
                        var oEl = that.getEl();
                        that.usernameIpt=oEl.find('div.js-username2');
                        that.pwdIpt = oEl.find('div.js-pwd2');
                        that.emailIpt = oEl.find('div.js-email2');
                        that.initCpn();
                    }
            }, {
            name: 'click a.js-register',
            handler: function (oEvent) {
                oEvent.preventDefault();
                var that = this;
                // 值检查
                if (!that.checkVal()) {
                    return;
                }
                var oData = that.val();
                $.ajax({
                    url: '/reg/',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        username: oData.username,
                        password: oData.pwd,
                        email:oData.email
                    }
                }).done(function (oResult) {
                    if (oResult.code === 0) {
//                        window.location.reload();
                        that.emit('register');
                    } else {
                       alert("注册失败！");
                      /*  oResult.msgname && that.iptError(that.usernameIpt, oResult.msgname);
                        oResult.msgpwd && that.iptError(that.pwdIpt, oResult.msgpwd);
                        oResult.msgemail && that.iptError(that.emailIpt, oResult.msgemail);*/
                    }
                }).fail(function () {
                    alert('出现错误，请重试');
                });
            }
        }],
        show: fStaticShow
    }, {
        initialize: fInitialize,
        initCpn: fInitCpn,
        val: fVal,
        checkVal: fCheckVal,
        iptSucc: fIptSucc,
        iptError: fIptError,
        iptNone: fIptNone
    });

    function fStaticShow(oConf) {
        var that = this;
        var oRegister = new PopupReigster(oConf);
        var oPopup = new Popup({
            width: 540,
            content: oRegister.html()
        });
        oRegister._popup = oPopup;
        Component.setEvents();
    }

    function fInitialize(oConf) {
        var that = this;
        delete oConf.renderTo;
        PopupReigster.superClass.initialize.apply(that, arguments);
    }

    function fInitCpn() {
        var that = this;
        that.usernameIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.usernameIpt));
        that.pwdIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.pwdIpt));
        that.emailIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.emailIpt));
    }

    function fVal(oData) {
        var that = this;
        var oUsernameIpt = that.usernameIpt.find('input');
        var oEmailIpt = that.emailIpt.find('input');
        var oPwdIpt = that.pwdIpt.find('input');

        if (arguments.length === 0) {
            return {
                username: $.trim(oUsernameIpt.val()),
                pwd: $.trim(oPwdIpt.val()),
                email: $.trim(oEmailIpt.val())
            };
        } else {
            oUsernameIpt.val($.trim(oData.username));
            oPwdIpt.val($.trim(oData.pwd));
            oEmailIpt.val($.trim(oData.email));
        }
    }

    function fCheckVal() {
        var that = this;
        var oData = that.val();
        var bRight = true;

        if (!oData.username) {
            that.iptError(that.usernameIpt, '用户名不能为空');
            bRight = false;
            return bRight;
        }

        if (!oData.pwd) {
            that.iptError(that.pwdIpt, '密码不能为空');
            bRight = false;
            return bRight;
        } else if (oData.pwd.length < 6) {
            that.iptError(that.pwdIpt, '密码不能小于6位');
            bRight = false;
            return bRight;
        }

         if (!Util.isEmail(oData.email)) {
             that.iptError(that.emailIpt, '请填写正确的邮箱');
             bRight = false;
             return bRight;
         }
        return bRight;
    }

    function fIptSucc(oIpt) {
        var that = this;
        oIpt = $(oIpt);
        that.iptNone(oIpt);
        oIpt.addClass('success');
        if (!oIpt.find('.icon-ok-sign').get(0)) {
            oIpt.append('<i class="input-icon icon-ok-sign"></i>');
        }
    }

    function fIptError(oIpt, sMsg) {
        var that = this;
        oIpt = $(oIpt);
        that.iptNone(oIpt);
        oIpt.addClass('error');
        if (!oIpt.find('.icon-remove-sign').get(0)) {
            oIpt.append('<i class="input-icon icon-remove-sign"></i>');
        }
        var oSpan = oIpt.find('.input-tip');
        if (!oSpan.get(0)) {
            oSpan = $('<span class="input-tip"></span>');
            oIpt.append(oSpan);
        }
        oSpan.html($.trim(sMsg));
    }

    function fIptNone(oIpt) {
        var that = this;
        $(oIpt).removeClass('error success');
    }
})(window);