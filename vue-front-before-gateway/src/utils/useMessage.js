// hooks/useMessage.js
import { message } from 'ant-design-vue';

export function useMessage() {
  const success = (content, duration = 3, onClose) => {
    return message.success({ content, duration, onClose });
  };

  const error = (content, duration = 3, onClose) => {
    return message.error({ content, duration, onClose });
  };

  const warning = (content, duration = 3, onClose) => {
    return message.warning({ content, duration, onClose });
  };

  const info = (content, duration = 3, onClose) => {
    return message.info({ content, duration, onClose });
  };

  const loading = (content, duration = 0, onClose) => {
    return message.loading({ content, duration, onClose });
  };

  const destroy = () => {
    message.destroy();
  };

  return {
    success,
    error,
    warning,
    info,
    loading,
    destroy,
  };
}