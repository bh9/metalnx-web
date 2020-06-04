<template>
<div>
<b-table striped head-variant="dark" hover :items="getGridData.items" :fields="getGridData.fields">
  <template slot="customTitle" slot-scope="data">
    <b-link v-bind:href="data.item.url_link" target="_blank">{{ data.item.title }}</b-link>
  </template>
</b-table>
</div>
</template>

<script>
  export default {
    data() {
    return {isActive: false}
    },
    name:'SearchStyleResultGrid',
    props:['searchResult'],
    computed: {
      getGridData:  function () {
        var resultData = [];
        for (var entry in this.searchResult.search_result) {
          // Converting properties array to dictionary
          var propertySet = this.searchResult.search_result[entry].properties.propertySet
          var propertyDict = {};
          for (var propertyEntry in propertySet){
            propertyDict[propertySet[propertyEntry].name] = propertySet[propertyEntry].value
          }
          this.searchResult.search_result[entry]['propertyDict'] = propertyDict
          resultData.push(this.searchResult.search_result[entry])
        }
        
        var data = {
        fields: [
          {
            key: 'customTitle',
            label: 'Name',
            formatter: value => {
              return value
            }
          },
          {
            key: 'propertyDict.lastModifiedDate',
            label: 'Modified',
            formatter: value => {
              return new Date(value * 1000)
            }
          },
          {
            key: 'propertyDict.dataSize',
            label: 'Size (kB)',
            formatter: value => {
              return value
            }
          }
        ],
        items: resultData
        }
        return data;
      }
    }
  }
</script>
