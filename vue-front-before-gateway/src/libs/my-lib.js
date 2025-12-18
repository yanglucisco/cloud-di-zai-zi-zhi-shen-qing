import { createElementBlock, openBlock, normalizeClass, renderSlot } from 'vue';

var script = {
  __name: 'MyButton',
  props: {
  type: String
},
  setup(__props) {



return (_ctx, _cache) => {
  return (openBlock(), createElementBlock("button", {
    class: normalizeClass(['my-btn', `my-btn--${__props.type}`]),
    onClick: _cache[0] || (_cache[0] = $event => (_ctx.$emit('click')))
  }, [
    renderSlot(_ctx.$slots, "default")
  ], 2 /* CLASS */))
}
}

};

function styleInject(css, ref) {
  if ( ref === void 0 ) ref = {};
  var insertAt = ref.insertAt;

  if (typeof document === 'undefined') { return; }

  var head = document.head || document.getElementsByTagName('head')[0];
  var style = document.createElement('style');
  style.type = 'text/css';

  if (insertAt === 'top') {
    if (head.firstChild) {
      head.insertBefore(style, head.firstChild);
    } else {
      head.appendChild(style);
    }
  } else {
    head.appendChild(style);
  }

  if (style.styleSheet) {
    style.styleSheet.cssText = css;
  } else {
    style.appendChild(document.createTextNode(css));
  }
}

var css_248z = "\n.my-btn[data-v-5abd212e] {\r\n  padding: 8px 16px;\r\n  border: none;\r\n  border-radius: 4px;\r\n  cursor: pointer;\n}\n.my-btn--default[data-v-5abd212e] {\r\n  background-color: #f0f0f0;\n}\n.my-btn--primary[data-v-5abd212e] {\r\n  background-color: #42b983;\r\n  color: white;\n}\r\n";
styleInject(css_248z);

script.__scopeId = "data-v-5abd212e";
script.__file = "src/components/MyButton.vue";

const components = [script];
const install = function (Vue) {
  if (install.installed) return;
  components.forEach((component) => {
    Vue.component(component.name, component);
  });
};
// 自动注册（当在浏览器环境中通过 <script> 标签引入时）
if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue);
}

// 导出 install 方法和每个组件
var index = {
  install,
  MyButton: script
};

export { index as default };
