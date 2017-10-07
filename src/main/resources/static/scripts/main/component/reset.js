/**
 * Created by Administrator on 2017/9/24.
 */
(function (window) {
    var Reset = Base.createClass('main.component.Reset');
    var Popup = Base.getClass('main.component.Popup');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');

    Base.mix(Reset, Component, {
        _tpl: [
            '<div class="wrapper-content clearfix">',
                '<div class="input-section">',
                    '<div class="form-group">',
                        '<label class="control-label">用户名</label>',
                        '<div class="control-group js-user"><input type="text" placeholder="请输入用户名"></div>',
                    '</div>',
                    '<div class="form-group">',
                        '<label class="control-label">重置密码</label>',
                        '<div class="control-group js-pass"><input type="password" placeholder="请输入重置密码"></div>',
                    '</div>',
                    '<div class="form-group">',
                        '<div class="col-input-login">',
                            '<a class="btn btn-info js-reset" href="javascript:void(0);">确认</a>',
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
                that.usernameIpt = oEl.find('div.js-user');
                that.pwdIpt = oEl.find('div.js-pass');
                that.initCpn();
            }
        }, {
            name: 'click a.js-reset',
            handler: function (oEvent) {
                oEvent.preventDefault();
                var that = this;
                // 值检查
                if (!that.checkVal()) {
                    return;
                }
                var oData = that.val();
                $.ajax({
                    url: '/reset',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        username: oData.usernameIpt,
                        password: oData.pwdIpt
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

    function fStaticShow(oConf) {
        var that = this;
        var oReset = new Reset(oConf);
        var oPopup = new Popup({
            title: '管理权限——重置密码',
            width: 540,
            content: oReset.html()
        });
        oReset._popup = oPopup;
        Component.setEvents();
    }

    function fInitialize(oConf) {
        var that = this;
        delete oConf.renderTo;
        Reset.superClass.initialize.apply(that, arguments);
    }

    function fInitCpn() {
        var that = this;
        that.usernameIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.usernameIpt));
        that.pwdIpt.find('input').on('focus', Base.bind(that.iptNone, that, that.pwdIpt));
    }

    function fVal(oData) {
        var that = this;
        var oUsernameIpt = that.usernameIpt.find('input');
        var oPwdIpt = that.pwdIpt.find('input');

        if (arguments.length === 0) {
            return {
                usernameIpt: $.trim(oUsernameIpt.val()),
                pwdIpt: $.trim(oPwdIpt.val())
            };
        } else {
            oUsernameIpt.val($.trim(oData.usernameIpt));
            oPwdIpt.val($.trim(oData.pwdIpt));
        }
    }

    function fCheckVal() {
        var that = this;
        var oData = that.val();
        var bRight = true;

        if (!oData.usernameIpt) {
            that.iptError(that.usernameIpt, '用户名不能为空');
            bRight = false;
            return bRight;
        }

        if (!oData.pwdIpt) {
            that.iptError(that.pwdIpt, '密码不能为空');
            bRight = false;
            return bRight;
        } else if (oData.pwdIpt.length < 6) {
            that.iptError(that.pwdIpt, '密码不能小于6位');
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
