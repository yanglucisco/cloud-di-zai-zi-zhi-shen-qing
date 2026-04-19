export function getOrgData() {
  const r = []
  for(let i = 1;i < 102;i++){
    r.push({
        key: i + "",
        OrgName: "John Brown " + i,
        classify: "公司",
        sort: 1,
    })
  }
  return r
}

export default {

}
