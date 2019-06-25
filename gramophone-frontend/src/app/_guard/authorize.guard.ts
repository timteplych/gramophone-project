import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

import {AuthenticationService} from '../_services';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.authenticationService.currentUserValue;
    if (currentUser) {
      if (route.data.roles.indexOf(currentUser.roles[0].name || currentUser.roles[1].name || currentUser.roles[2].name) === -1) {
        this.router.navigate(['/']);
        return false;
      }
      return true;
    }

    this.router.navigate(['/']);
    return false;
  }
}
