import { allOrg } from "@/testData/org"
import request from '@/utils/request' 

export function getAllOrgs() {
  request('/account/org/page').then(res => {
    var d = res;
  })
}

export function getOrgData() {
  const r = []
  for(let i = 1;i < 20 + Math.floor(Math.random() * 100);i++){
    r.push({
        key: i + "",
        orgName: "John Brown " + i,
        classify: "公司",
        sort: 1,
    })
  }
  debugger
  for(const item of allOrg){
    r.push(item);
  }
  return r
}

export function getOrgData1(page, pageSize) {
  const r = []
  for(let i = 0;i < pageSize;i++){
    const index = page * pageSize + i
    r.push({
        key: i + "",
        orgName: "John Brown " + index,
        classify: "公司",
        sort: index,
    })
  }
  for(const item of allOrg){
    r.push(item);
  }
  return r
}

export default {

}
