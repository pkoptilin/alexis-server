import { maxValue } from '../constants/constants';

const filteerSuccess = arr => arr.filter(word => word.success >= maxValue);
const filteerInProcess = arr => arr.filter(word => word.success < maxValue);

export { filteerSuccess, filteerInProcess };
