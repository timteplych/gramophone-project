import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'buttonSubFormatPipe'
})
export class ButtonSubFormatPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value) {
      const suffixes = ['ТЫС.', 'МЛН.', 'МЛРД.', 'ТРЛН.', 'P', 'E'];

      if (value < 1000) {
        return value;
      }

      const exp = Math.floor(Math.log(value) / Math.log(1000));
      console.log(exp, (value / Math.pow(1000, exp)).toFixed(args).split('.')[1]);

      if ((value / Math.pow(1000, exp)).toFixed(1).split('.')[1] === '0') {
        return (value / Math.pow(1000, exp)).toFixed(args) + ' ' + suffixes[exp - 1];
      } else {
        return (value / Math.pow(1000, exp)).toFixed(1).replace('.', ',') + ' ' + suffixes[exp - 1];
      }
    }
    return value;
  }

}
