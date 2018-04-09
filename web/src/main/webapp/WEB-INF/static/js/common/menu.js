layui.define('jquery', function(exports){
  "use strict";
  
  var $ = layui.$
  ,hint = layui.hint();
  
  var enterSkin = 'layui-tree-enter', Menu = function(options){
    this.options = options;
  };
  
  //初始化
  Menu.prototype.init = function(elem){
    var that = this;
    elem.addClass('layui-box layui-menu'); //添加tree样式
    that.tree(elem);
    that.on(elem);
  };
  
  //树节点解析
  Menu.prototype.tree = function(elem, children){
    var that = this, options = that.options
    var nodes = children || options.nodes;
    
    layui.each(nodes, function(index, item){
      var hasChild = item.children && item.children.length > 0;
      var ul = $('<ul class="'+ (item.spread ? "layui-show" : "") +'"></ul>');
      var li = $(['<li '+ (item.spread ? 'data-spread="'+ item.spread +'"' : '') +'>'
        
        //节点
        ,function(){
          return '<a href="javascript:;" class="layui-menu-link" ><i class="'
              +(hasChild ? (item.spread ? 'layui-menu-spreaded' : 'layui-menu-to-spread'):'layui-menu-leaf') + ' fa fa-'
              + (item.icon || 'envira') +'"></i><cite>'
              + (item.name||'未命名') +'</cite></a>';
        }()
      
      ,'</li>'].join(''));
      
      //如果有子节点，则递归继续生成树
      if(hasChild){
        li.append(ul);
        that.tree(ul, item.children);
      }
      
      elem.append(li);
      
      //触发点击节点回调
      typeof options.click === 'function' && that.click(li, item); 
      
      //伸展节点
      that.spread(li, item);

    });
  };
  
  //点击节点回调
  Menu.prototype.click = function(elem, item){
    var that = this, options = that.options;
    elem.children('a').on('click', function(e){
      layui.stope(e);
      options.click(item)
    });
  };
  
  //伸展节点
  Menu.prototype.spread = function(elem, item){
    var that = this, options = that.options;
    var ul = elem.children('ul'), a = elem.children('a');
    var iconColor = a.children('.fa');
    
    //执行伸展
    var open = function(){
      if(elem.data('spread')){
        elem.data('spread', null)
        ul.removeClass('layui-show');
        iconColor.removeClass('layui-menu-spreaded').addClass('layui-menu-to-spread');
      } else {
        elem.data('spread', true);
        ul.addClass('layui-show');
        iconColor.removeClass('layui-menu-to-spread').addClass('layui-menu-spreaded');
      }
    };
    
    //如果没有子节点，则不执行
    if(!ul[0]) return;

    a.on('click', open);
  }
  
  //通用事件
  Menu.prototype.on = function(elem){
    //屏蔽选中文字
    elem.find('i').on('selectstart', function(e){
      return false
    });
  };
  
  //暴露接口
  exports('menu', function(options){
    var menu = new Menu(options = options || {});
    var elem = $(options.elem);
    if(!elem[0]){
      return hint.error('layui.menu 没有找到'+ options.elem +'元素');
    }
    menu.init(elem);
  });
});
