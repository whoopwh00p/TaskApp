import { NgModule } from '@angular/core';

import { MatDialogModule } from '@angular/material/dialog';
import {MatGridListModule} from '@angular/material/grid-list';
import { FormsModule } from '@angular/forms';
import {MatListModule} from '@angular/material/list';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
@NgModule({
  exports: [FormsModule, MatDialogModule, MatGridListModule,MatListModule,MatInputModule,MatSelectModule]
})
export class MaterialModule {}