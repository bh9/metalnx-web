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
          resultData.push(this.searchResult.search_result[entry])
        }
        var data = {
        fields: [
          {
            key: 'customTitle',
            label: 'Title',
            formatter: value => {
              return value
            }
          },
          {
            key: 'subtitle',
            label: 'Subtitle',
            formatter: value => {
              return value
            }
          },
          {
            key: 'content_text',
            label: 'Content text',
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
