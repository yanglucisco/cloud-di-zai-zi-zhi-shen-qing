export function startOneTimeTimer(delay, callback) {
  setTimeout(() => {
    callback();
    startOneTimeTimer(delay, callback);
  }, delay);
}