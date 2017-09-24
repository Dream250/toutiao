(function (window) {
    var Modify = Base.createClass('main.component.Modify');
    var Popup = Base.getClass('main.component.Popup');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');

    Base.mix(Modify, Component, {
        _tpl: [
            '<div class="wrapper-content clearfix">',
                '<div class="input-section">',
                    '<div class="form-group">',
                        '<label class="control-label">用户名</label>',
                        '<label  class="control-label" id="name"></label>',
                        '<script>',
                        'var value = localhost;',
                        'document.getElementById("name").innerHTML=value',
                        '</script>',
                    '</div>',
                    '<div class="form-group">',
                        '<label class="control-label">原密码</label>',
                        '<div class="control-group js-pwd"><input type="password" placeholder="请输入原密码"></div>',
                    '</div>',

                    '<div class="form-group">',
                        '<label class="control-label">修改密码</label>',
                        '<div class="control-group js-pwd1"><input type="password" placeholder="请输入修改密码"></div>',
                    '</div>',

                    '<div class="form-group">',
                        '<label class="control-label">确认密码</label>',
                        '<div class="control-group js-pwd2"><input type="password" placeholder="请输入确认密码"></div>',
                    '</div>',
                    '<div class="form-group">',
                        '<div class="col-input-login">',
                            /*'<a class="btn btn-info js-login" href="javascript:void(0);">登录</a>',*/
                            '<a class="btn btn-info js-modify" href="javascript:void(0);">确认</a>',
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
                        that.usernameIpt=window.localhost;
                        that.pwdIpt = oEl.find('div.js-pwd');
                        that.pwdIpt1 = oEl.find('div.js-pwd1');
                        that.pwdIpt2 = oEl.find('div.js-pwd2');
                        that.initCpn();
                    }
            }, {
            name: 'click a.js-modify',
            handler: function (oEvent) {
                oEvent.preventDefault();
                var that = this;
                // 值检查
                if (!that.checkVal()) {
                    return;
                }
                var oData = that.val();
                $.ajax({
                    url: '/modify',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        username: localhost,
                        password: oData.pwdIpt,
                        password1: oData.pwdIpt1
                    }
                }).done(function (oResult) {
                    if (oResult.code === 0) {
                        window.location.reload();
                        alert(oResult.msg);
                    } else {
                       alert(oResult.msg);
                    }
                }).fail(function () {
                    alert("修改失败!");
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

    function fStaticShow(oConf,localhost) {
        var that = this;
        window.localhost = localhost;
        var oRegister = new Modify(oConf);
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
        Modify.superClass.initialize.apply(that, arguments);
    }

    function fInitCpn() {
        var that = this;
        that.pwdIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.pwdIpt));
        that.pwdIpt1.find('input').on('focus', Base.bind(that.iptNone, that, that.pwdIpt1));
        that.pwdIpt2.find('input').on('focus', Base.bind(that.iptNone, that, that.pwdIpt2));
    }

    function fVal(oData) {
        var that = this;
        var oPwdIpt = that.pwdIpt.find('input');
        var oPwdIpt1 = that.pwdIpt1.find('input');
        var oPwdIpt2 = that.pwdIpt2.find('input');

        if (arguments.length === 0) {
            return {
                pwdIpt: $.trim(oPwdIpt.val()),
                pwdIpt1: $.trim(oPwdIpt1.val()),
                pwdIpt2: $.trim(oPwdIpt2.val())
            };
        } else {
            oPwdIpt.val($.trim(oData.pwdIpt));
            oPwdIpt1.val($.trim(oData.pwdIpt1));
            oPwdIpt2.val($.trim(oData.pwdIpt2));
        }
    }

    function fCheckVal() {
        var that = this;
        var oData = that.val();
        var bRight = true;

        if (!oData.pwdIpt) {
            that.iptError(that.pwdIpt, '原密码不能为空');
            bRight = false;
            return bRight;
        }

        if (!oData.pwdIpt1) {
            that.iptError(that.pwdIpt1, '修改密码不能为空');
            bRight = false;
            return bRight;
        } else if (oData.pwdIpt1.length < 6) {
            that.iptError(that.pwdIpt1, '密码不能小于6位');
            bRight = false;
            return bRight;
        }

        if (!oData.pwdIpt2) {
            that.iptError(that.pwdIpt2, '确认密码不能为空');
            bRight = false;
            return bRight;
        } else if (oData.pwdIpt2 != oData.pwdIpt1) {
            that.iptError(that.pwdIpt2, '确认密码错误');
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